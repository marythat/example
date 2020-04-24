package com.newsupplytech.projectapplication.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import springfox.documentation.service.Contact;


@Data
@Component
@ConfigurationProperties(prefix = "sysconfig")
public class SysConfigProp {
    private String projectName;
    private String version;
    private String desc;
    private Contact contact;
}
