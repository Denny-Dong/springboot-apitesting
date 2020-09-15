package com.listenrobot.test.web.flow;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class JiraFlow {

    List<String> severityLevel = Arrays.asList("一般", "致命", "严重", "提示");

    public Boolean isImpactIssue(List responseJsonList) {
        Boolean flag = false;
        Map jsonMap = (Map) responseJsonList.get(responseJsonList.size() - 1);
        List itemsList = (List) jsonMap.get("items");
        Map itemMap = (Map) itemsList.get(itemsList.size() - 1);
        if (itemMap.get("field").equals("缺陷严重等级") && severityLevel.contains(itemMap.get("fromString"))) {
            flag = true;
        }
        return flag;
    }

}
