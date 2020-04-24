package com.newsupplytech.projectapplication.comm.core;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 服务配置
 */
@Data
@Configuration
@ConditionalOnExpression("!'${nst}'.isEmpty()")
@ConfigurationProperties(prefix = "nst")
public class NstProperties {

    private String url;

    private String ftpserver;

    private String ftpuser;

    private String ftppwd;

    private Integer ftpport;

    private String ftpgetdir;

}
