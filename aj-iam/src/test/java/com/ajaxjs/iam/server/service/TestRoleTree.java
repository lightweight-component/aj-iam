package com.ajaxjs.iam.server.service;

import com.ajaxjs.framework.entity.tree.FlatArrayToTree;
import com.ajaxjs.iam.permission.Permission;
import com.ajaxjs.iam.permission.PermissionService;
import com.ajaxjs.iam.server.BaseTest;
import com.ajaxjs.sqlman.Sql;
import com.ajaxjs.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class TestRoleTree extends BaseTest {
    @Test
    public void test() {
        List<Map<String, Object>> nodes = Sql.newInstance().input("SELECT * FROM per_role").queryList();
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
}
