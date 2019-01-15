package com.listenrobot.test.web;

import com.listenrobot.test.web.web.user.User_Steps;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RunWith(Suite.class)
@SuiteClasses({User_Steps.class})
@SpringBootApplication
public class WebRunner {

}
