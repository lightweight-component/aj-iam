package com.ajaxjs.iam.client;

import com.ajaxjs.iam.client.filter.UserInterceptor;
import com.ajaxjs.iam.jwt.JWebTokenMgr;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.http_request.Get;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.SimpleTraceInterceptor;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.ServletRequestHandledEvent;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Configuration
//@ComponentScan("com.ajaxjs.iam.client")
@Slf4j
public class AutoConfiguration implements WebMvcConfigurer {
    @Value("${auth.iam_service: }")
    private String iamService;

    /**
     * JWT 验证的密钥
     */
    @Value("${User.oidc.jwtSecretKey:Df87sD#$%#A}")
    private String jwtSecretKey;

    /**
     * JWT 解密
     */
    @Bean
    JWebTokenMgr jWebTokenMgr() {
        testIamConnection();
        JWebTokenMgr mgr = new JWebTokenMgr();
        mgr.setSecretKey(jwtSecretKey);

        return mgr;
    }



    private void testIamConnection() {
        ResponseEntityExceptionHandler l;
        ServletRequestHandledEvent k;
        if (StrUtil.hasText(iamService)) {
            CompletableFuture.runAsync(() -> {        // 异步执行任务
                try {
                    Map<String, Object> api = Get.api(iamService);

                    if (api == null || !api.containsKey("status") || (int) api.get("status") != 1)
                        log.warn("IAM 服务连接失败或异常。你的认证服务或不可用。");
                    else
                        log.info("IAM 服务连接成功");
                } catch (Throwable e) {
                    log.warn("testIamConnection", e);
                }
            });
        }
    }

    @Bean
    UserInterceptor userInterceptor() {
        return new UserInterceptor();
    }

    @Value("${auth.excludes: }")
    private String excludes;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptor = registry.addInterceptor(userInterceptor());
        interceptor.addPathPatterns("/**").excludePathPatterns("/favicon.ico"); // 拦截所有

        // 不需要的拦截路径
        if (StrUtil.hasText(excludes)) {
            String[] arr = excludes.split(",|\\|");
            interceptor.excludePathPatterns(arr);
        }
    }
}
