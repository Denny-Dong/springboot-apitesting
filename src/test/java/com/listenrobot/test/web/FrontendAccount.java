package com.listenrobot.test.web;

import com.listenrobot.test.web.framework.ConfigBean;
import io.restassured.RestAssured;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FrontendAccount {
    @Autowired
    ConfigBean configBean;

    @Bean(name = "getToken")
    public String getToken(@Autowired @Qualifier(value = "defaultDriver") RestAssured restAssured) {
        Map<String, String> accountInfo = new HashMap<String, String>();
        accountInfo.put("account", configBean.getUsername());
        accountInfo.put("password", configBean.getPassword());
        String token = "Bearer " + restAssured.given().body(accountInfo)
                .when().post("/iam/oauth/token")
                .then().assertThat().statusCode(200)
                .extract().body().jsonPath().getJsonObject("token");
        return token;
    }


}
