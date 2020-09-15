package com.listenrobot.test.web.web.user;

import com.listenrobot.test.web.flow.JiraFlow;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class User_Steps {

    JiraFlow jiraFlow = new JiraFlow();

    public Boolean isIssueImpacted(String issueId) {
        List<Map<String, Object>> responseJsonList = RestAssured
                .given().pathParam("issueId", issueId).queryParam("expand", "changelog")
                .when().get("/rest/api/2/issue/{issueId}")
                .then().statusCode(200)
                .extract().body().jsonPath().get("changelog.histories");
        return jiraFlow.isImpactIssue(responseJsonList);
    }

    @Test
    public void getIssueListFromJQL() {
        List<String> fields = Collections.singletonList("key");
        Map<String, Object> searchParameter = new HashMap<>();
        searchParameter.put("jql", "project = MDCS AND issuetype = 故障 AND 缺陷严重等级 =  无");
        searchParameter.put("startAt", 0);
        searchParameter.put("maxResults", 50);
        searchParameter.put("fields", fields);
        List<Map<String, Object>> issueList = RestAssured
                .given().body(searchParameter)
                .when().post("/rest/api/2/search")
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().body().jsonPath().get("issues");
        List<String> keyList = jiraFlow.compositeKeyList(issueList);
        List<String> impactKeyList = new ArrayList<>();
        keyList.forEach(key -> {
            if(isIssueImpacted(key)){
                impactKeyList.add(key);
            }
        });
        System.out.println(impactKeyList);
    }


}
