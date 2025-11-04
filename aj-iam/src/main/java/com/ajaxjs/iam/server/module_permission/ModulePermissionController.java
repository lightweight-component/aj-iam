package com.ajaxjs.iam.server.module_permission;

import com.ajaxjs.framework.mvc.unifiedreturn.BizAction;
import com.ajaxjs.iam.permission.Permission;
import com.ajaxjs.iam.permission.PermissionControl;
import com.ajaxjs.iam.permission.PermissionService;
import com.ajaxjs.iam.permission.Role;
import com.ajaxjs.sqlman.Action;
import com.ajaxjs.util.ObjectHelper;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/module_permission")
public class ModulePermissionController {
    /**
     * 根据角色 id 获取其权限列表
     *
     * @param roleId 角色 id
     * @return 权限列表
     */
    @GetMapping("/permission_list_by_role/{roleId}")
    @BizAction("根据角色 id 获取其权限列表")
    public List<Permission> getPermissionListByRole(@PathVariable Integer roleId) {
        Role role = new Action("SELECT * FROM per_role WHERE id = ?").query(roleId).one(Role.class);
        Objects.requireNonNull(role, "There is NO role, id:" + roleId);
        List<Permission> result = new ArrayList<>();
        // get all permission lists
        List<Permission> allPermissionList = getAllPermissionList();
        Long permissionValue = role.getModuleValue();

        if (permissionValue != null && permissionValue != 0)
            PermissionService.getPermissionList(result, allPermissionList, permissionValue, false, null);

        // find parents
        if (role.getIsInheritedParent()) {
            List<Role> parentRoles = new Action("WITH RECURSIVE parent_cte AS (\n" +
                    "  SELECT id, name, parent_id, module_value FROM per_role\n" +
                    "  WHERE id = ?  -- 用您要查询的节点的ID替换 <your_node_id>\n" +
                    "  UNION ALL\n" +
                    "  SELECT pr.id, pr.name, pr.parent_id, pr.module_value FROM per_role pr\n" +
                    "  INNER JOIN parent_cte pc ON pr.id = pc.parent_id\n" +
                    ")\n" +
                    "SELECT id, name, parent_id, module_value\n" +
                    "FROM parent_cte WHERE id != ? -- 不包含自己").query(roleId, roleId).list(Role.class);

            if (!CollectionUtils.isEmpty(parentRoles)) {
                for (Role r : parentRoles)
                    PermissionService.getPermissionList(result, allPermissionList, r.getModuleValue(), true, r.getName());
            }
        }

        return PermissionService.removeDuplicates(result);
    }

    private List<Permission> getAllPermissionList() {
        return new Action("SELECT * FROM per_module WHERE stat = 0 ORDER BY id ASC").query().list(Permission.class);
    }

    /**
     * 为角色添加权限 id 列表
     *
     * @param roleId        角色 id
     * @param permissionIds 权限列表
     * @return 是否成功
     */
    @PostMapping("/add_permissions_to_role")
    @BizAction("为角色添加权限 id 列表")
    public boolean addPermissionsToRole(@RequestParam Integer roleId, @RequestParam List<Integer> permissionIds) {
        List<Integer> allPermissionIIdList = new Action("SELECT id FROM per_module WHERE stat = 0 ORDER BY id ASC").query().list(Integer.class);
        int[] indexes = PermissionService.findIndexes(permissionIds, allPermissionIIdList);
//        System.out.println(allPermissionIIdList);
//        System.out.println(Arrays.toString(indexes));
        long num = 0L;
        for (int index : indexes) {
            if (index == -1)
                throw new IllegalStateException("找不到权限");

            num = PermissionControl.set(num, index, true);//设置[权限项 index]为true
        }

//        log.info("permissionValue: " + num);
        Map<String, Object> map = ObjectHelper.mapOf("id", roleId, "module_value", num);

        return new Action(map, "per_role").update().withId().isOk();
    }
}
