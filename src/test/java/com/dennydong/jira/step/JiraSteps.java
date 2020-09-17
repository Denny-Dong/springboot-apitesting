package com.dennydong.jira.step;

import com.dennydong.jira.flow.JiraFlow;
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
public class JiraSteps {

    JiraFlow jiraFlow = new JiraFlow();

    @Test
    public void recoverIssue() {
        List<Map<String, String>> impactKeyList = jiraFlow.getIssueListFromJQL(10);
        if (impactKeyList.size()!=0){
            impactKeyList.forEach(stringStringMap -> {
                System.out.println("修复 : " + stringStringMap);
                jiraFlow.updateField(stringStringMap.get("issueId"), stringStringMap.get("sseverityLevel"));
            });
        }else {
            System.out.println("待修复列表为零，不需要修复");
        }

    }


}
