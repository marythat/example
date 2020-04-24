package com.newsupplytech.projectapplication.config;

import com.newsupplytech.projectapplication.comm.core.interceptor.RestApiInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Value("${spring.resources.static-locations}")
    private String staticLocations;
    @Value("${nst.url}")
    private String url;
    @Autowired
    private RestApiInterceptor restApiInterceptor;

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        /* 是否允许请求带有验证信息 */
        corsConfiguration.setAllowCredentials(true);
        /* 允许访问的客户端域名 */
        corsConfiguration.addAllowedOrigin("*");
        /* 允许服务端访问的客户端请求头 */
        corsConfiguration.addAllowedHeader("*");
        /* 允许访问的方法名,GET POST等 */
        corsConfiguration.addAllowedMethod("*");
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        new CorsFilter(urlBasedCorsConfigurationSource);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

    @Bean
    public HttpFilter filter(){
        return new HttpFilter() {
            @Override
            public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
                response.setHeader("Access-Control-Max-Age", "3600");
                response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type,token,iv,mobile,Accept, Connection, User-Agent, Cookie,loginId,trainerStatus");
                response.setHeader("Access-Control-Expose-Headers", "Origin, X-Requested-With, Content-Type,token,iv,mobile,Accept, Connection, User-Agent, Cookie,loginId,trainerStatus");
                filterChain.doFilter(request, response);
            }
        };
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(restApiInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/swagger-resources/**","/api/auth/tokenPair","/**/*.js","/","/static/**","/commAttachment/downloadFile/**","/commAttachment/ftpDownload/**","/commAttachment/download/**");
    }

    /**
     * 静态资源的配置 - 使得可以从磁盘中读取 Html、图片、视频、音频等
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations(staticLocations.split(","));
    }

    /**
     * 方案一： 默认访问根路径跳转 doc.html页面 （swagger文档页面）
     * 方案二： 访问根路径改成跳转 index.html页面 （简化部署方案： 可以把前端打包直接放到项目的 webapp，上面的配置）
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index.html");
    }
}
