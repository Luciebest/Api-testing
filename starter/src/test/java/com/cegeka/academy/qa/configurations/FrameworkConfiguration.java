package com.cegeka.academy.qa.configurations;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.*;

import javax.annotation.PreDestroy;

@Configuration
@ComponentScan(basePackages = "com.cegeka.academy.qa")
public class FrameworkConfiguration {
    @Bean
    WebDriver getApi() {
        return null;
    }

}
