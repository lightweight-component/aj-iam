package com.ajaxjs.iam.server.service;

import com.ajaxjs.framework.database.EnableTransaction;
import com.ajaxjs.iam.server.controller.AdminController;
import com.ajaxjs.iam.server.model.RoleToUserDTO;
import com.ajaxjs.spring.annotation.BizAction;
import com.ajaxjs.sqlman.Action;
import com.ajaxjs.sqlman.page.PageResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class AdminService implements AdminController {
    @Override
    @BizAction("欢迎页")
    public Map<String, Object> welcome() {
        Integer tenantId = TenantService.getTenantId();

        return new Action("SELECT\n" +
                "        (SELECT COUNT(id) FROM user_login_log WHERE tenant_id = ?) AS loginTimes,\n" +
                "        (SELECT COUNT(id) FROM user WHERE stat != 1 AND tenant_id = ?) AS userNum,\n" +
                "        (SELECT COUNT(id) FROM app WHERE stat != 1) AS clientNum,\n" +
                "        (SELECT COUNT(id) FROM access_token WHERE tenant_id = ?) AS onlineNum,\n" +
                "        (SELECT COUNT(id) FROM tenant WHERE stat != 1) AS tenantNum").query(tenantId, tenantId, tenantId).one();
    }

    @Override
    public PageResult<Map<String, Object>> userRoleList(HttpServletRequest req) {
        StringBuilder sql = new StringBuilder("""
                SELECT user.*, t_join_table1.name AS tenantName, t_join_table1.default_role_id AS defaultRoleId, default_role.name AS defaultRoleName,
                    CAST(COALESCE((
                        SELECT JSON_ARRAYAGG(JSON_OBJECT('id', r.id, 'name', r.name)) OVER (
                            ORDER BY r.id ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
                        )
                        FROM per_role r
                        WHERE r.id IN (SELECT ur.role_id FROM per_user_role ur WHERE ur.user_id = user.id)
                        LIMIT 1
                    ), JSON_ARRAY()) AS JSON) AS roles
                FROM user
                LEFT JOIN tenant t_join_table1 ON user.tenant_id = t_join_table1.id
                LEFT JOIN per_role default_role ON t_join_table1.default_role_id = default_role.id
                WHERE user.stat != 1
                """);
        List<Object> params = new ArrayList<>();
        String loginId = req.getParameter("loginId");

        if (loginId != null && !loginId.isBlank()) {
            String keyword = loginId.strip();
            sql.append("AND (LOCATE(?, user.login_id) > 0 OR LOCATE(?, user.username) > 0)\n");
            params.add(keyword);
            params.add(keyword);
        }

        appendTextFilter(sql, params, req.getParameter("phone"), "AND LOCATE(?, user.phone) > 0\n");
        appendTextFilter(sql, params, req.getParameter("email"), "AND LOCATE(?, user.email) > 0\n");

        String tenantId = req.getParameter("tenantId");
        if (tenantId != null && !tenantId.isBlank()) {
            sql.append("AND user.tenant_id = ?\n");
            params.add(Integer.valueOf(tenantId.strip()));
        }

        sql.append("ORDER BY user.create_date DESC");
        return new Action(sql.toString()).query(params.toArray()).pageByPageNo(req);
    }

    private static void appendTextFilter(StringBuilder sql, List<Object> params, String value, String predicate) {
        if (value != null && !value.isBlank()) {
            sql.append(predicate);
            params.add(value.strip());
        }
    }

    @Override
    @EnableTransaction
    public boolean saveRoleToUser(RoleToUserDTO dto) {
        if (dto == null || dto.getUserId() == null || dto.getUserId() <= 0
                || dto.getRoleIds() == null
                || dto.getRoleIds().stream().anyMatch(id -> id == null || id <= 0))
            return false;

        Long userId = dto.getUserId();
        // 按用户加锁，使同一用户的角色写入和去重顺序执行。
        Map<String, Object> existingUser = new Action("""
                SELECT t.default_role_id AS defaultRoleId
                FROM user u
                LEFT JOIN tenant t ON t.id = u.tenant_id
                WHERE u.id = ? AND u.stat != 1
                FOR UPDATE OF u FOR SHARE OF t
                """).query(userId).one();
        if (existingUser == null)
            return false;

        Number defaultRoleId = (Number) existingUser.get("defaultRoleId");
        List<Long> roleIds = dto.getRoleIds().stream()
                .filter(id -> defaultRoleId == null || id != defaultRoleId.longValue())
                .distinct()
                .toList();
        String placeholders = String.join(", ", Collections.nCopies(roleIds.size(), "?"));
        if (!roleIds.isEmpty()) {
            String findRoles = "SELECT id FROM per_role WHERE stat != 1 AND id IN (" + placeholders + ") FOR SHARE";
            if (new Action(findRoles).query(roleIds.toArray()).list(Long.class).size() != roleIds.size())
                return false;
        }

        // 只同步角色树中可编辑的关系，保留默认角色和已删除角色的历史关系。
        StringBuilder delete = new StringBuilder("""
                DELETE ur FROM per_user_role ur
                JOIN per_role r ON r.id = ur.role_id AND r.stat != 1
                WHERE ur.user_id = ?
                """);
        List<Object> deleteParams = new ArrayList<>();
        deleteParams.add(userId);
        if (defaultRoleId != null) {
            delete.append("AND ur.role_id <> ?\n");
            deleteParams.add(defaultRoleId);
        }
        if (!roleIds.isEmpty()) {
            delete.append("AND ur.role_id NOT IN (").append(placeholders).append(")");
            deleteParams.addAll(roleIds);
        }
        new Action(delete.toString()).update(deleteParams.toArray()).execute();

        if (roleIds.isEmpty())
            return true;

        String insert = """
                INSERT INTO per_user_role (user_id, role_id)
                SELECT ?, r.id FROM per_role r
                LEFT JOIN per_user_role existing ON existing.user_id = ? AND existing.role_id = r.id
                WHERE r.id IN (%s) AND existing.id IS NULL
                """.formatted(placeholders);
        List<Object> params = new ArrayList<>();
        params.add(userId);
        params.add(userId);
        params.addAll(roleIds);

        // 没有新增角色时影响行数为 0，重复提交也算成功。
        return new Action(insert).update(params.toArray()).execute().isOk();
    }

}
