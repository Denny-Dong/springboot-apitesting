package com.listenrobot.test.web.web.user;

import io.restassured.RestAssured;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class User_Steps {

    @Autowired
    @Qualifier(value = "initDriver")
    RestAssured restAssured;


    @Test
    public void test() {
        restAssured.given().when().get("/user/v1/users/protocol/status").then().statusCode(200).body(
                "status",
                equalTo(true));
    }


}
