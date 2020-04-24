package com.newsupplytech.projectapplication.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Autowired
    RestTemplateBuilder restTemplateBuilder;
    /**
     * 在配置类中使用restTemplateBuilder构建RestTemplate实例
     * */
    @Bean
    public RestTemplate restTemplate(){
        return restTemplateBuilder.build();
    }

}
