package com.dennydong.jira.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jira")
@Data
public class ConfigBean {
    String username;
    String password;
    String baseUrl;
    Long responseTimeLimit;
}
