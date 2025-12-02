package com.ajaxjs.iam.permission;

import com.ajaxjs.iam.BaseTest;
import com.ajaxjs.iam.server.module_permission.PermissionService;
import com.ajaxjs.util.ObjectHelper;
import com.ajaxjs.util.httpremote.Get;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestSDK extends BaseTest {
    /**
     * 权限列表
     */
    interface PermissionList {
        PermissionEntity USER_MANAGE = new PermissionEntity("USER_MANAGE");
        PermissionEntity BASE_MANAGE = new PermissionEntity("BASE_MANAGE");
    }

    @Test
    public void test() {
        PermissionService.init(PermissionList.class);

        boolean check = PermissionList.USER_MANAGE.check(257);
        System.out.println(check);
        check = PermissionList.BASE_MANAGE.check(513);
        System.out.println(check);
    }

    public class RddPermissionConfig implements PermissionConfig {
        public static final PermissionEntity MAIN_MODULE_PERMISSION = new PermissionEntity("ZE_BH");

        public static final PermissionEntity ADMIN_PERMISSION = new PermissionEntity("ZE_BH_Admin");

        @Override
        public PermissionEntity getMainModulePermission() {
            return MAIN_MODULE_PERMISSION;
        }

        @Override
        public List<PermissionEntity> getModulePermissions() {
            return ObjectHelper.listOf(ADMIN_PERMISSION);
        }
    }

    void getModulePermissions(PermissionConfig config) {
        List<String> permissionCodes = new ArrayList<>();
        PermissionEntity mainModulePermission = config.getMainModulePermission();
        permissionCodes.add(mainModulePermission.getName());

        if (!ObjectHelper.isEmpty(config.getModulePermissions()))
            config.getModulePermissions().forEach(permission -> permissionCodes.add(permission.getName()));

        Map<String, Object> result = Get.api("http://localhost:8082/iam_api/permission/get_index_by_code?permissionCodes=" + String.join(",", permissionCodes) + "&type=module");

        if (result != null && result.containsKey("status") && "1".equals(result.get("status").toString())) {
            Map<String, Object> data = (Map<String, Object>) result.get("data");

            Object index = data.get(mainModulePermission.getName());
            mainModulePermission.setIndex((int) index);

            if (!ObjectHelper.isEmpty(config.getModulePermissions()))
                config.getModulePermissions().forEach(permission -> {
                    Object index2 = data.get(permission.getName());
                    permission.setIndex((int) index2);
                });
        }
    }

    @Test
    void testGetModulePermissions() {
        RddPermissionConfig config = new RddPermissionConfig();
    }
}
