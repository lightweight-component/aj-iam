package com.ajaxjs.iam.permission;

import java.util.List;

/**
 * 客户端模块权限配置
 */
public interface PermissionConfig {
    /**
     * 获取主模块权限
     *
     * @return 主模块权限
     */
    PermissionEntity getMainModulePermission();

    /**
     * 获取模块权限列表
     *
     * @return 模块权限列表
     */
    List<PermissionEntity> getModulePermissions();
}
