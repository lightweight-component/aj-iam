package com.ajaxjs.iam.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ComponentScan("com.ajaxjs.iam")
public class IamServerApp {
    public static void main(String[] args) {
        SpringApplication.run(IamServerApp.class, args);
        System.out.println("=========================================(♥◠‿◠)ﾉﾞ【AJ-IAM-Server 启动成功】ლ(´ڡ`ლ)ﾞ==================================================");
    }
}
