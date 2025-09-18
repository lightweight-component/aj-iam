package com.ajaxjs.iam.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限拦截检查
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PermissionCheck {
    /**
     * 模块权限 CODE 标识
     *
     * @return 权限 CODE 标识
     */
    String modulePermissionCode() default "";
}
