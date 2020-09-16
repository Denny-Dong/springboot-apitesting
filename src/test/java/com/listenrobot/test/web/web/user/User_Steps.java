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

    public Map<String, String> isIssueImpacted(String issueId) {
        List<Map<String, Object>> responseJsonList = RestAssured
                .given().pathParam("issueId", issueId).queryParam("expand", "changelog")
                .when().get("/rest/api/2/issue/{issueId}")
                .then().statusCode(200)
                .extract().body().jsonPath().get("changelog.histories");
        return jiraFlow.isImpactIssue(issueId, responseJsonList);
    }

    public List<Map<String, String>> getIssueListFromJQL(Integer maxResults) {
        List<String> fields = Collections.singletonList("key");
        Map<String, Object> searchParameter = new HashMap<>();
        searchParameter.put("jql", "project = MDCS AND issuetype = 故障 AND 缺陷严重等级 =  无");
        searchParameter.put("startAt", 0);
        searchParameter.put("maxResults", maxResults);
        searchParameter.put("fields", fields);
        List<Map<String, Object>> issueList = RestAssured
                .given().body(searchParameter)
                .when().post("/rest/api/2/search")
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().body().jsonPath().get("issues");
        List<String> keyList = jiraFlow.compositeKeyList(issueList);
        List<Map<String, String>> impactKeyList = new ArrayList<>();
        keyList.forEach(key -> {
            Map<String, String> severityMap = isIssueImpacted(key);
            if (severityMap.size() != 0) {
                impactKeyList.add(severityMap);
            }
        });
        System.out.println("impactKeyList : " + impactKeyList);
        System.out.println("impactKeyList size : " + impactKeyList.size());
        return impactKeyList;
    }

    public void updateField(String issueId, String severityLevel) {
        Map<String, String> valueMap = Collections.singletonMap("value", severityLevel);
        Map<String, Object> fieldMap = Collections.singletonMap("customfield_10600", valueMap);
        Map<String, Map<String, Object>> postBody = Collections.singletonMap("fields", fieldMap);
        RestAssured.given().log().ifValidationFails().body(postBody).pathParam("issueId", issueId)
                .when().put("/rest/api/2/issue/{issueId}")
                .then().log().ifValidationFails().assertThat().statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    public void recoverIssue() {
        List<Map<String, String>> impactKeyList = getIssueListFromJQL(1000);
        if (impactKeyList.size()!=0){
            impactKeyList.forEach(stringStringMap -> {
                System.out.println("修复 : " + stringStringMap);
                updateField(stringStringMap.get("issueId"), stringStringMap.get("sseverityLevel"));
            });
        }else {
            System.out.println("待修复列表为零，不需要修复");
        }

    }


}
