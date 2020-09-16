package com.listenrobot.test.web.flow;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;

import java.util.*;

public class JiraFlow {

    List<String> severityLevel = Arrays.asList("一般", "致命", "严重", "提示");

    public Map<String, String> isImpactIssue(String issueId, List responseJsonList) {
        Map<String, String> severityMap = new HashMap<>();
        if (responseJsonList.size() != 0) {
            Map<String, Map<String, Object>> jsonMap = (Map<String, Map<String, Object>>) responseJsonList.get(responseJsonList.size() - 1);
            if (jsonMap.get("author").get("name").equals("dongyi")) {
                List itemsList = (List) jsonMap.get("items");
                Map itemMap = (Map) itemsList.get(itemsList.size() - 1);
                if (itemMap.get("field").equals("缺陷严重等级") && severityLevel.contains(itemMap.get("fromString"))) {
                    severityMap.put("issueId", issueId);
                    severityMap.put("sseverityLevel", (String) itemMap.get("fromString"));
                }
            }
        } else {
            System.out.println("responseJsonList 为空");
        }
        return severityMap;
    }


    public List<String> compositeKeyList(List<Map<String, Object>> issueList) {
        List<String> keyList = new ArrayList<>();
        issueList.forEach(issueMap -> {
            keyList.add((String) issueMap.get("key"));
        });
        return keyList;
    }

    public Map<String, String> isIssueImpacted(String issueId) {
        List<Map<String, Object>> responseJsonList = RestAssured
                .given().pathParam("issueId", issueId).queryParam("expand", "changelog")
                .when().get("/rest/api/2/issue/{issueId}")
                .then().statusCode(200)
                .extract().body().jsonPath().get("changelog.histories");
        return isImpactIssue(issueId, responseJsonList);
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
        List<String> keyList = compositeKeyList(issueList);
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
}
