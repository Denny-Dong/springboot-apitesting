package com.dennydong.jira.step;

import com.dennydong.jira.config.ConfigBean;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.lessThan;
import static io.restassured.RestAssured.*;

@Component
public class BaseStep {
    @Autowired
    ConfigBean configBean;

    @Bean
    public void baseConfig() {
        PreemptiveBasicAuthScheme preemptiveBasicAuthScheme = new PreemptiveBasicAuthScheme();
        preemptiveBasicAuthScheme.setUserName(configBean.getUsername());
        preemptiveBasicAuthScheme.setPassword(configBean.getPassword());

        requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(configBean.getBaseUrl())
                .setAuth(preemptiveBasicAuthScheme)
                .build();
        responseSpecification = new ResponseSpecBuilder()
                .expectResponseTime(lessThan(configBean.getResponseTimeLimit()), TimeUnit.SECONDS)
                .build();
        enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
