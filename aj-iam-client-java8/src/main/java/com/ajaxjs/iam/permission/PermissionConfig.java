package com.ajaxjs.iam.permission;

import java.util.List;

public interface PermissionConfig {
    /**
     * 获取主模块权限
     *
     * @return 主模块权限
     */
    PermissionEntity getMainModulePermission();

    List<PermissionEntity> getModulePermissions();
}
