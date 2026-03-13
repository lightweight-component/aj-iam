package com.ajaxjs.iam.client;

import com.ajaxjs.iam.client.filter.UserInterceptor;
import com.ajaxjs.iam.jwt.JWebTokenMgr;
import com.ajaxjs.iam.permission.PermissionConfig;
import com.ajaxjs.iam.permission.PermissionEntity;
import com.ajaxjs.util.ObjectHelper;
import com.ajaxjs.util.httpremote.Get;
import com.ajaxjs.util.httpremote.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;
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
    @Value("${auth:Df87sD#$%#A}")
    private String jwtSecretKey;

    /**
     * JWT 解密
     */
    @Bean
    JWebTokenMgr jWebTokenMgr() {
        testIamConnection();
        getPermissions();

        JWebTokenMgr mgr = new JWebTokenMgr();
        mgr.setSecretKey(jwtSecretKey);

        return mgr;
    }

    @Autowired(required = false)
    private PermissionConfig permissionConfig;

    private void getPermissions() {
        if (permissionConfig != null) {
            CompletableFuture.runAsync(() -> {        // 异步执行任务
                getModulePermissions(permissionConfig);
                log.info("通过 IAM 获取权限值成功");
            });
        }
    }

    /**
     * 测试IAM服务连接状态
     * <p>
     * 该方法用于检测IAM认证服务的可用性，通过异步方式调用IAM服务的API接口来验证连接状态。
     * 如果当前运行在IAM本体环境中则跳过检测，否则根据配置的iamService地址进行连接测试。
     * <p>
     * 检测结果将通过日志输出，成功时记录INFO级别日志，失败时记录WARN级别日志。
     */
    private void testIamConnection() {
        // 如果在 IAM 本体，根本不需要检测，都还未有服务，检查啥
        if (ClassUtils.isPresent("com.ajaxjs.iam.server.IamServerApp", null))
            return;

        if (ObjectHelper.hasText(iamService)) {
            CompletableFuture.runAsync(() -> {        // 异步执行任务
                try {
                    Map<String, Object> api = Get.api(iamService + "/iam_api/");

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
        if (ObjectHelper.hasText(excludes)) {
            String[] arr = excludes.split(",|\\|");
            interceptor.excludePathPatterns(arr);
        }
    }

    /**
     * 获取模块权限信息
     * <p>
     * 该方法通过调用远程 IAM API 获取指定模块的权限索引信息，并将结果设置到权限配置对象中。
     * 主要包括主模块权限和子模块权限的处理。
     *
     * @param config 权限配置对象，包含主模块权限和子模块权限列表
     */
    void getModulePermissions(PermissionConfig config) {
        List<String> permissionCodes = new ArrayList<>();
        PermissionEntity mainModulePermission = config.getMainModulePermission();
        permissionCodes.add(mainModulePermission.getName());

        if (!ObjectHelper.isEmpty(config.getModulePermissions()))
            config.getModulePermissions().forEach(permission -> permissionCodes.add(permission.getName()));

        Map<String, Object> result = Get.api(iamService + "/iam_api/permission/get_index_by_code?permissionCodes=" + String.join(",", permissionCodes) + "&type=module");

        if (Response.isOk(result)) {
            Map<String, Object> data = (Map<String, Object>) result.get("data");

            Object index = data.get(mainModulePermission.getName());
            mainModulePermission.setIndex((int) index);

            if (!ObjectHelper.isEmpty(config.getModulePermissions()))
                config.getModulePermissions().forEach(permission -> {
                    Object index2 = data.get(permission.getName());
                    permission.setIndex((int) index2);
                });

            log.info("init permission ok.");
        }
    }
}
