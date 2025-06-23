package com.ajaxjs.iam.server.config;

import com.ajaxjs.base.Sdk;
import com.ajaxjs.framework.filter.google_captcha.GoogleCaptchaInterceptor;
import com.ajaxjs.framework.spring.database.ConnectionMgr;
import com.ajaxjs.iam.server.service.OidcService;
import com.ajaxjs.iam.user.common.session.ServletUserSession;
import com.ajaxjs.iam.user.common.session.UserSession;
import com.ajaxjs.util.JsonUtil;
import com.ajaxjs.util.cache.Cache;
import com.ajaxjs.util.cache.expiry.ExpiryCache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.util.function.Function;

import static com.ajaxjs.iam.server.common.IamConstants.JWT_TOKEN_USER_KEY;

@Configuration
@Slf4j
public class IamConfig implements WebMvcConfigurer {
    @Value("${db.url}")
    private String url;

    @Value("${db.user}")
    private String user;

    @Value("${db.psw}")
    private String psw;

    @Bean(value = "dataSource", destroyMethod = "close")
    DataSource getDs() {
        return ConnectionMgr.setupJdbcPool("com.mysql.cj.jdbc.Driver", url, user, psw);
    }

    /**
     * 用户全局拦截器
     */
    @Bean
    UserInterceptor authInterceptor() {
        return new UserInterceptor();
    }

    @Bean
    @Qualifier("getuserfromjvmhash")
    Function<String, String> getUserFromJvmHash() {
        return token -> {
            Cache<String, Object> cache = simpleJvmCache();
            String key = JWT_TOKEN_USER_KEY + "-" + token;
            OidcService.TokenUser tokenUser = cache.get(key, OidcService.TokenUser.class);

            if (tokenUser == null)
                throw new SecurityException("找不到用户信息");

            return JsonUtil.toJson(tokenUser.getAccessToken());
        };
    }

    @Value("${auth.excludes: }")
    private String excludes;

    @Value("${GoogleCaptcha.accessKeyId}")
    private String googleCaptchaAccessKeyId;

    @Value("${GoogleCaptcha.accessSecret}")
    private String googleCaptchaAccessSecret;

    /**
     * 拦截器
     */
    @Bean
    GoogleCaptchaInterceptor googleCaptchaMvcInterceptor() {
        GoogleCaptchaInterceptor g = new GoogleCaptchaInterceptor();
        g.setAccessSecret(googleCaptchaAccessSecret);

        return g;
    }

    /**
     * 加入认证拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("初始化 SSO 拦截器");
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(authInterceptor());
        registry.addInterceptor(googleCaptchaMvcInterceptor());
        interceptorRegistration.addPathPatterns("/**"); // 拦截所有

        // 不需要的拦截路径
        if (StringUtils.hasText(excludes)) {
            String[] arr = excludes.split("\\|");
            interceptorRegistration.excludePathPatterns(arr);
        }
    }

    @Bean
    Cache<String, Object> simpleJvmCache() {
        return ExpiryCache.getInstance();
    }

    @Bean
    UserSession UserSession() {
        return new ServletUserSession();
    }

    @Value("${BaseService.endPoint}")
    private String baseServiceEndPoint;

    @Bean
    Sdk initBaseSDK() {
        Sdk sdk = new Sdk();
        sdk.setEndPoint(baseServiceEndPoint);
        sdk.setRestTemplate(new RestTemplate());

        return sdk;
    }

//    @Bean
//    CrossFilter CrossFilter() {
//        return new CrossFilter();
//    }

    /**
     * 跨域
     *
     * @param registry 注册跨域
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedHeaders("*").allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE").allowedOrigins("*").allowCredentials(false);
    }

//    @Bean
//    public CorsFilter corsFilter() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.addAllowedOrigin("*");
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//
//        return new CorsFilter(source);
//    }
}
