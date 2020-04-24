package com.newsupplytech.projectapplication.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@Slf4j
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class Swagger2Config implements WebMvcConfigurer {
    @Autowired
    private SysConfigProp configProp;

    /**
     *
     * 显示swagger-ui.html文档展示页，还必须注入swagger资源：
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * swagger2的配置文件，这里可以配置swagger2的一些基本的内容，比如扫描的包等等
     *
     * @return Docket
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //此包路径下的类，才生成接口文档
                .apis(RequestHandlerSelectors.basePackage("com.newsupplytech.projectapplication.modules"))
                //加了ApiOperation注解的类，才生成接口文档
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(securitySchemes());
        //.globalOperationParameters(setHeaderToken());
    }

    /***
     * oauth2配置
     * 需要增加swagger授权回调地址
     * http://localhost:8080/webjars/springfox-swagger-ui/o2c.html
     * @return
     */
    @Bean
    List<SecurityScheme> securitySchemes() {
        List<SecurityScheme> securitySchemes = Lists.newArrayList();
        securitySchemes.add(new ApiKey("token", "token", "header"));
        securitySchemes.add(new ApiKey("iv", "iv", "header"));
        return securitySchemes;
    }
    /**
     * JWT token
     * @return
     */
//    private List<Parameter> setHeaderToken() {
//        List<Parameter> pars = new ArrayList<>();
//        ParameterBuilder tokenPar = new ParameterBuilder();
//        tokenPar.name("token").description("token").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
//        pars.add(tokenPar.build());
//        return pars;
//    }

    /**
     * api文档的详细信息函数,注意这里的注解引用的是哪个
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(configProp.getProjectName()+" 后台服务API接口文档")
                .version(configProp.getVersion())
                .description(configProp.getDesc())
                .contact(configProp.getContact())
                .build();
    }

}
