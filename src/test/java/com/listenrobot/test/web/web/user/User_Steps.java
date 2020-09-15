package com.listenrobot.test.web.web.user;

import com.listenrobot.test.web.flow.JiraFlow;
import io.restassured.RestAssured;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class User_Steps {
    @Autowired
    RestAssured restAssured;

    @Test
    public void test() {
        List responseJsonList = restAssured.given().pathParam("issueId", "MDCS-819").queryParam("expand", "changelog")
                .when().get("/rest/api/2/issue/{issueId}")
                .then().statusCode(200)
                .extract().body().jsonPath().get("changelog.histories");
        JiraFlow jiraFlow = new JiraFlow();
        System.out.println(jiraFlow.isImpactIssue(responseJsonList));

    }


}
