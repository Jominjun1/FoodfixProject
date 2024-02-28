package com.project.foodfix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.project.foodfix" ,exclude = {SecurityAutoConfiguration.class})
public class FoodFixApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoodFixApplication.class, args);
    }
}
