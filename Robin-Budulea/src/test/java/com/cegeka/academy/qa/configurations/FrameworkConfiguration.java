package com.cegeka.academy.qa.configurations;


import org.openqa.selenium.WebDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.cegeka.academy.qa")
public class FrameworkConfiguration {
    @Bean
    WebDriver getApi() {
        return null;
    }

}
