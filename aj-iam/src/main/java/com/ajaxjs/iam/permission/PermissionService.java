package com.ajaxjs.iam.permission;


import com.ajaxjs.framework.tree.FlatArrayToTree;
import com.ajaxjs.iam.server.controller.PermissionController;
import com.ajaxjs.sqlman.Sql;
import com.ajaxjs.sqlman.crud.Entity;
import com.ajaxjs.sqlman.model.UpdateResult;
import com.ajaxjs.util.ObjectHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PermissionService implements PermissionController {
    @Override
    public List<Map<String, Object>> getRoleTree() {
        List<Map<String, Object>> nodes = Sql.newInstance().input("SELECT * FROM per_role WHERE stat != 1").queryList();
        // 扁平化的列表转换为 tree 结构
        List<Map<String, Object>> data = new FlatArrayToTree().mapAsTree(Integer.class, nodes);

        return transformToTreeStructure(data);
    }

    @Override
    public boolean deleteRole(Integer id) {
        String sql = "WITH RECURSIVE sub_roles AS (" +
                "  SELECT id, parent_id, name FROM per_role WHERE id = ?" +
                "  UNION ALL" +
                "  SELECT r.id, r.parent_id, r.name FROM per_role r INNER JOIN sub_roles sr ON sr.id = r.parent_id" +
                ")" +
                "UPDATE per_role SET stat = 1 WHERE id IN (SELECT id FROM sub_roles);";

        return Sql.newInstance().input(sql, id).update().isOk();
    }

    @Override
    public List<Permission> getPermissionListByRole(Integer roleId) {
        Role role = Sql.newInstance().input("SELECT * FROM per_role WHERE id = ?", roleId).query(Role.class);
        Objects.requireNonNull(role, "There is NO role, id:" + roleId);
        List<Permission> result = new ArrayList<>();
        // get all permission list
        List<Permission> allPermissionList = getAllPermissionList();
        Long permissionValue = role.getPermissionValue();

        if (permissionValue != null && permissionValue != 0)
            getPermissionList(result, allPermissionList, permissionValue, false, null);

        // find parents
        if (role.getIsInheritedParent()) {
            List<Role> parentRoles = Sql.newInstance().input("WITH RECURSIVE parent_cte AS (\n" +
                    "  SELECT id, name, parent_id, permission_value FROM per_role\n" +
                    "  WHERE id = ?  -- 用您要查询的节点的ID替换 <your_node_id>\n" +
                    "  UNION ALL\n" +
                    "  SELECT pr.id, pr.name, pr.parent_id, pr.permission_value FROM per_role pr\n" +
                    "  INNER JOIN parent_cte pc ON pr.id = pc.parent_id\n" +
                    ")\n" +
                    "SELECT id, name, parent_id, permission_value\n" +
                    "FROM parent_cte WHERE id != ? -- 不包含自己", roleId, roleId).queryList(Role.class);

            if (!CollectionUtils.isEmpty(parentRoles)) {
                for (Role r : parentRoles)
                    getPermissionList(result, allPermissionList, r.getPermissionValue(), true, r.getName());
            }
        }

        return removeDuplicates(result);
    }

    private List<Permission> getAllPermissionList() {
        return Sql.newInstance().input("SELECT * FROM per_permission WHERE stat = 0 ORDER BY id ASC").queryList(Permission.class);
    }

    @Override
    public boolean addPermissionsToRole(Integer roleId, List<Integer> permissionIds) {
        List<Integer> allPermissionIIdList = Sql.newInstance().input("SELECT id FROM per_permission WHERE stat = 0 ORDER BY id ASC").queryList(Integer.class);
        int[] indexes = findIndexes(permissionIds, allPermissionIIdList);
//        System.out.println(allPermissionIIdList);
//        System.out.println(Arrays.toString(indexes));
        long num = 0L;
        for (int index : indexes) {
            if (index == -1)
                throw new IllegalStateException("找不到权限");

            num = PermissionControl.set(num, index, true);//设置[权限项 index]为true
        }

        log.info("permissionValue: " + num);
        Map<String, Object> map = ObjectHelper.mapOf("id", roleId, "permission_value", num);
        UpdateResult updateResult = Entity.newInstance().setTableName("per_role").input(map).update();

        return updateResult.isOk();
    }

    /**
     * 查找索引
     *
     * @param ids                  需要查找的 ID 列表
     * @param allPermissionIIdList 所有权限 ID 列表
     * @return 包含索引的数组
     */
    private static int[] findIndexes(List<Integer> ids, List<Integer> allPermissionIIdList) {
        int[] result = new int[ids.size()];

        for (int i = 0; i < ids.size(); i++) {
            Integer id = ids.get(i);
            result[i] = allPermissionIIdList.indexOf(id);
        }

        return result;
    }

    // 去重
    private static List<Permission> removeDuplicates(List<Permission> list) {
        return list.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Permission::getId))),
                ArrayList::new
        ));
    }

    /**
     * 获取权限列表
     *
     * @param result            返回权限列表
     * @param allPermissionList 所有权限列表
     * @param permissionValue   权限值
     * @param isInherited       是否继承
     * @param roleName          父角色名称
     */
    private void getPermissionList(List<Permission> result, List<Permission> allPermissionList, Long permissionValue, boolean isInherited, String roleName) {
        int i = 0;

        for (Permission p : allPermissionList) {
            if (PermissionControl.check(permissionValue, i++)) {
                if (isInherited) {
                    p.setIsInherit(true);
                    p.setRoleName(roleName);
                }

                result.add(p);
            }
        }
    }

    // a stupid method
//    public List<Permission> getPermissionListByRole(Integer roleId) {
//        Map<String, Object> role = CRUD.infoMap("SELECT * FROM per_role WHERE id = ?", roleId);
//        Long permissionValue = (Long) role.get("permissionValue");
//
//        if (permissionValue == null || permissionValue == 0)
//            return null;
//
//        // get all permission list
//        List<Permission> allPermissionList = CRUD.list(Permission.class, "SELECT * FROM per_permission WHERE stat = 0 ORDER BY id ASC");
//        String binaryString = Long.toBinaryString(permissionValue);
//
//        if (!StringUtils.hasText(binaryString))
//            throw new NullPointerException("没有 permissionValue");
//
//        System.out.println(binaryString);
//
//        char[] arr = binaryString.toCharArray();
//
//        if (arr.length > allPermissionList.size())
//            throw new IllegalStateException("不足够的权限");
//
//        int j = 0;
//        List<Permission> result = new ArrayList<>();
//        for (int i = arr.length - 1; i >= 0; i--) {
//            if (arr[i] == '1') {
//                Permission permission = allPermissionList.get(j);
//                result.add(permission);
//            }
//
//            j++;
//        }
//
//        return result;
//    }

    /**
     * 将扁平的列表转换为 iView 的 tree 结构
     */
    private List<Map<String, Object>> transformToTreeStructure(List<Map<String, Object>> data) {
        List<Map<String, Object>> iView = new ArrayList<>();

        for (Map<String, Object> map : data) {
            Map<String, Object> iViewMap = new HashMap<>();
            iViewMap.put("id", map.get("id"));
            iViewMap.put("title", map.get("name"));
            iViewMap.put("parentId", map.get("parentId"));
            iViewMap.put("expand", true);
            iViewMap.put("contextmenu", true);

            Object _children = map.get("children");

            if (_children != null) {
                List<Map<String, Object>> children = (List<Map<String, Object>>) _children;

                if (children.size() > 0)
                    iViewMap.put("children", transformToTreeStructure(children));
            }

            iView.add(iViewMap);
        }

        return iView;
    }

    /**
     * 初始化权限
     *
     * @param permissionListClz 权限列表的类，一般是接口
     */
    public static void init(Class<?> permissionListClz) {
        List<String> allPermissionIIdList = Sql.newInstance().input("SELECT code FROM per_permission WHERE stat = 0 ORDER BY id ASC").queryList(String.class);
        Field[] fields = permissionListClz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            // 获取字段的值
            try {
                Object value = field.get(null);  // 由于常量是静态的，所以传入null作为对象实

                if (value instanceof PermissionEntity) {
                    PermissionEntity permissionEntity = (PermissionEntity) value;
                    int index = allPermissionIIdList.indexOf(permissionEntity.getName());

                    if (index == -1)
                        throw new IllegalStateException("找不到权限");

                    permissionEntity.setIndex(index);
                    log.info("Field: " + permissionEntity);
                }
            } catch (IllegalAccessException e) {
                log.error("获取字段值时出错：" + e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }
}
