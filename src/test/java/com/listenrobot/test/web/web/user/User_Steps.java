package com.listenrobot.test.web.web.user;

import io.restassured.RestAssured;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class User_Steps {
    @Autowired
    RestAssured restAssured;

    @Test
    public void test() {
        List jsonList = restAssured.given().pathParam("issueId", "MDCS-1264").queryParam("expand", "changelog")
                .when().get("/rest/api/2/issue/{issueId}")
                .then().statusCode(200)
                .extract().body().jsonPath().get("changelog.histories");
        Map jsonMap = (Map) jsonList.get(jsonList.size() - 1);
        List itemsList = (List) jsonMap.get("items");
        System.out.println(itemsList.get(itemsList.size()-1));
    }


}
