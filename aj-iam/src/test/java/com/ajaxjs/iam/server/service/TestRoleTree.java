package com.ajaxjs.iam.server.service;


import com.ajaxjs.framework.tree.FlatArrayToTree;
import com.ajaxjs.iam.BaseTest;
import com.ajaxjs.iam.permission.Permission;
import com.ajaxjs.iam.permission.PermissionService;
import com.ajaxjs.sqlman.Action;
import com.ajaxjs.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestRoleTree extends BaseTest {
    @Test
    public void test() {
        List<Map<String, Object>> nodes = new Action("SELECT * FROM per_role").query().list();
        // 扁平化的列表转换为 tree 结构
        List<Map<String, Object>> tree = new FlatArrayToTree().mapAsTree(Integer.class, nodes);

        System.out.println(JsonUtil.toJson(tree));
    }

    @Autowired
    PermissionService permissionService;

    @Test
    public void testGetPermissionListByRole() {
        List<Permission> permissionListByRole = permissionService.getPermissionListByRole(28);
        System.out.println(permissionListByRole);
    }

    @Test
    void testGetPermission() {
        String sql = "SELECT module_value, permission_value FROM per_role WHERE id IN (SELECT role_id FROM per_user_role WHERE user_id = ?)";
        List<Map<String, Object>> result = new Action(sql).query(1).list();

        List<Long> permissions = new ArrayList<>();
        List<Long> modulePermissions = new ArrayList<>();

        if (result != null && !result.isEmpty()) {
            result.forEach(item -> {
                Object _permissionValue = item.get("permissionValue");

                if (_permissionValue != null) {
                    Long permissionValue = (Long) _permissionValue;

                    if (permissionValue != 0L)
                        permissions.add(permissionValue);
                }

                Object _moduleValue = item.get("moduleValue");

                if (_moduleValue != null) {
                    Long moduleValue = (Long) _moduleValue;

                    if (moduleValue != 0L)
                        modulePermissions.add(moduleValue);
                }
            });
        }

        Long[][] _permissions = new Long[2][];

        _permissions[0] = permissions.isEmpty() ? null : permissions.toArray(new Long[0]);
        _permissions[1] = modulePermissions.isEmpty() ? null : modulePermissions.toArray(new Long[0]);
    }
}
