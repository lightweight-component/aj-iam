package com.ajaxjs.iam.server.config;

import com.ajaxjs.framework.cache.Cache;
import com.ajaxjs.framework.cache.delayqueue.ExpiryCache;
import com.ajaxjs.framework.cache.lru.LRUCache;
import com.ajaxjs.iam.client.CacheProvider;
import com.ajaxjs.iam.server.common.session.ServletUserSession;
import com.ajaxjs.iam.server.common.session.UserSession;
import com.ajaxjs.iam.server.service.OidcService;
import com.ajaxjs.message.email.ISendEmail;
import com.ajaxjs.message.email.resend.Resend;
import com.ajaxjs.security.captcha.image.ImageCaptchaConfig;
import com.ajaxjs.security.captcha.image.impl.SimpleCaptchaImage;
import com.ajaxjs.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.function.Function;

import static com.ajaxjs.iam.server.common.IamConstants.JWT_TOKEN_USER_KEY;

@Configuration
@Slf4j
public class IamConfig implements WebMvcConfigurer {
    /**
     * 用户全局拦截器
     */
//    @Bean
//    UserInterceptor authInterceptor() {
//        return new UserInterceptor();
//    }
    @Bean
    @Qualifier("getuserfromjvmhash")
    Function<String, String> getUserFromJvmHash() {
        return token -> {
            Cache<String, Object> cache = ExpiryCache.getInstance();
            String key = JWT_TOKEN_USER_KEY + "-" + token;
            OidcService.TokenUser tokenUser = cache.get(key, OidcService.TokenUser.class);

            if (tokenUser == null)
                throw new SecurityException("找不到用户信息");

            return JsonUtil.toJson(tokenUser.getAccessToken());
        };
    }

    @Value("${auth.excludes: }")
    private String excludes;

//    @Value("${GoogleCaptcha.accessKeyId}")
//    private String googleCaptchaAccessKeyId;
//
//    @Value("${GoogleCaptcha.accessSecret}")
//    private String googleCaptchaAccessSecret;

    /**
     * 拦截器
     */
//    @Bean
//    GoogleCaptchaInterceptor googleCaptchaMvcInterceptor() {
//        GoogleCaptchaInterceptor g = new GoogleCaptchaInterceptor();
//        g.setAccessSecret(googleCaptchaAccessSecret);
//
//        return g;
//    }

    @Value("${aj-framework.message.email.resend.apikey}")
    private String sendEmailApiKey;

    @Bean
    ISendEmail initSendMail() {
        Resend sendEmail = new Resend();
        sendEmail.setApiKey(sendEmailApiKey);

        return sendEmail;
    }

    @Bean
    @Qualifier("localCache")
    Cache<String, Object> initLocalCache() {
        return new LRUCache<>(500);
    }

    @Bean
    ImageCaptchaConfig imageCaptchaConfig() {
        Cache<String, Object> cache = initLocalCache();
        ImageCaptchaConfig config = new ImageCaptchaConfig();
        config.setCaptchaImageProvider(new SimpleCaptchaImage());
        config.setSaveToRam(cache::put);
        config.setCaptchaCodeFromRam(key -> {
            Object o = cache.get(key);
            return o == null ? null : o.toString();
        });
        config.setRemoveByKey(cache::remove);

        return config;
    }

    @Bean
    CacheProvider initCacheProvider() {
        Cache<String, Object> cache = initLocalCache();
        return new CacheProvider() {
            @Override
            public void save(String key, String value, int expireSeconds) {
                cache.put(key, value, expireSeconds);
            }

            @Override
            public String get(String key) {
                return cache.get(key, String.class);
            }

            @Override
            public void remove(String key) {
                cache.remove(key);
            }
        };
    }

    /**
     * 加入认证拦截器
     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        log.info("初始化 SSO 拦截器");
//        InterceptorRegistration interceptorRegistration = registry.addInterceptor(authInterceptor()).order(2);
////        registry.addInterceptor(googleCaptchaMvcInterceptor());
//        interceptorRegistration.addPathPatterns("/**").excludePathPatterns("/favicon.ico"); // 拦截所有
//
//        // 不需要的拦截路径
//        if (StrUtil.hasText(excludes)) {
//            String[] arr = excludes.split(",|\\|");
//            interceptorRegistration.excludePathPatterns(arr);
//        }
//    }
//    @Bean
//    Cache<String, Object> simpleJvmCache() {
//        return ExpiryCache.getInstance();
//    }
    @Bean
    UserSession UserSession() {
        return new ServletUserSession();
    }
}
