package com.dennydong.jira.api.step;

import com.dennydong.jira.api.config.ConfigBean;
import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.lessThan;

@Component
public class BaseStep {
    @Autowired
    ConfigBean configBean;

    @Bean
    public void baseDriver() {
        PreemptiveBasicAuthScheme preemptiveBasicAuthScheme = new PreemptiveBasicAuthScheme();
        preemptiveBasicAuthScheme.setUserName(configBean.getUsername());
        preemptiveBasicAuthScheme.setPassword(configBean.getPassword());

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(configBean.getBaseUrl())
                .setAuth(preemptiveBasicAuthScheme)
                .build();
        RestAssured.responseSpecification = new ResponseSpecBuilder()
                .expectResponseTime(lessThan(configBean.getResponseTimeLimit()), TimeUnit.SECONDS)
                .build();
    }
}
