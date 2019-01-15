package com.listenrobot.test.web.framework;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.lessThan;

@Component
public class Driver {

    @Autowired
    ConfigBean configBean;

    @Bean(name = "defaultDriver")
    @Scope("prototype")
    public RestAssured defaultDriver() {
        RestAssured restAssured = new RestAssured();
        restAssured.baseURI = configBean.getBaseUrl();
        restAssured.port = configBean.getPort();
        restAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        restAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        restAssured.responseSpecification = new ResponseSpecBuilder()
                .expectResponseTime(lessThan(configBean.getResponseTimeLimit()), TimeUnit.SECONDS).build();
        return restAssured;
    }

    @Bean(name = "initDriver")
    public RestAssured initDriver(@Autowired @Qualifier(value = "defaultDriver") RestAssured restAssured,
                                  @Autowired @Qualifier(value = "getToken") String stringToken) {
        restAssured.requestSpecification = new RequestSpecBuilder().addHeader("Authorization", stringToken).build();
        return restAssured;
    }
}
