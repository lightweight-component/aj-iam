package com.ajaxjs.iam.server.common;

import com.ajaxjs.iam.permission.PermissionConfig;
import com.ajaxjs.iam.permission.PermissionEntity;
import com.ajaxjs.util.ObjectHelper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IamPermissionConfig implements PermissionConfig {
    public static final PermissionEntity ADMIN_PERMISSION = new PermissionEntity("IAM_ADMIN");

    @Override
    public PermissionEntity getMainModulePermission() {
        return null;
    }

    @Override
    public List<PermissionEntity> getModulePermissions() {
        return ObjectHelper.listOf(ADMIN_PERMISSION);
    }
}
