package com.ajaxjs.iam.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.ajaxjs.iam")
public class IamServerApp {
    public static void main(String[] args) {
        SpringApplication.run(IamServerApp.class, args);
    }
}
