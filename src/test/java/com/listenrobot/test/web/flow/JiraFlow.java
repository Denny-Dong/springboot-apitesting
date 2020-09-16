package com.listenrobot.test.web.flow;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JiraFlow {

    List<String> severityLevel = Arrays.asList("一般", "致命", "严重", "提示");

    public Map<String, String> isImpactIssue(String issueId, List responseJsonList) {
        Map<String, String> severityMap = new HashMap<>();
        if (responseJsonList.size()!=0){
            Map<String,Map<String,Object>> jsonMap = (Map<String, Map<String,Object>>) responseJsonList.get(responseJsonList.size() - 1);
            if (jsonMap.get("author").get("name").equals("dongyi")){
                List itemsList = (List) jsonMap.get("items");
                Map itemMap = (Map) itemsList.get(itemsList.size() - 1);
                if (itemMap.get("field").equals("缺陷严重等级") && severityLevel.contains(itemMap.get("fromString"))) {
                    severityMap.put("issueId",issueId);
                    severityMap.put("sseverityLevel", (String) itemMap.get("fromString"));
                }
            }
        }else {
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
}
