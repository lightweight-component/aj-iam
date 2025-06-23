package com.ajaxjs.iam.server;


import com.ajaxjs.embeded_tomcat.EmbeddedTomcatStarter;
import com.ajaxjs.embeded_tomcat.TomcatConfig;
import com.ajaxjs.framework.BaseWebMvcConfigure;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan({"com.ajaxjs.iam.server", "com.ajaxjs.iam.user", "com.ajaxjs.iam.permission"})
public class IamServerApp extends BaseWebMvcConfigure {
    public static void main(String[] args) {
        TomcatConfig cfg = new TomcatConfig();
        cfg.setEnableJsp(true);
        cfg.setPort(8888);
        cfg.setContextPath("/iam");

        new EmbeddedTomcatStarter(cfg, new Class[]{IamServerApp.class}).start();
    }
}
