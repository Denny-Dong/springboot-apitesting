package com.listenrobot.test.web.framework;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "web.frontend")
@Data
public class ConfigBean {
    String username;
    String password;
    String baseUrl;
    int port;
    Long responseTimeLimit;


}
