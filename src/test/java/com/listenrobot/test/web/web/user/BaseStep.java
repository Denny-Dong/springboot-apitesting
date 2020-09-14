package com.listenrobot.test.web.web.user;

import com.listenrobot.test.web.framework.ConfigBean;
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
    RestAssured baseDriver() {

        PreemptiveBasicAuthScheme preemptiveBasicAuthScheme = new PreemptiveBasicAuthScheme();
        preemptiveBasicAuthScheme.setUserName(configBean.getUsername());
        preemptiveBasicAuthScheme.setPassword(configBean.getPassword());

        RestAssured restAssured = new RestAssured();
        restAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(configBean.getBaseUrl())
                .setAuth(preemptiveBasicAuthScheme)
                .build();
        restAssured.responseSpecification = new ResponseSpecBuilder()
                .expectResponseTime(lessThan(configBean.getResponseTimeLimit()), TimeUnit.SECONDS)
                .build();

        return restAssured;
    }
}
