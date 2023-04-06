package com.cegeka.academy.qa.tests.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {
                "pretty",
                "json:target/cucumber-report/cucumber.json"
        },
        features = "src/test/resources/features",
        glue = {"com.cegeka.academy.qa.tests.steps"},
        stepNotifications = true
)
public class TestRunnerApi {
}
