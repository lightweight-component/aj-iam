package com.ajaxjs.iam;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching//开启缓存
@ComponentScan(basePackages = "com.ajaxjs.iam")
public class BaseConfig {
}
