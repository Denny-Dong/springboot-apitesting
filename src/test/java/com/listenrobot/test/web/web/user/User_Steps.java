package com.listenrobot.test.web.web.user;

import io.restassured.RestAssured;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class User_Steps {
    @Autowired
    RestAssured restAssured;

    @Test
    public void test() {
        restAssured.given().pathParam("issueId", "MDCS-1183").queryParam("expand", "changelog")
                .when().get("/rest/api/2/issue/{issueId}")
                .then().log().all().statusCode(200);
    }


}
