package com.ajaxjs.iam.server.controller;

import com.ajaxjs.iam.annotation.PermissionCheck;
import com.ajaxjs.iam.server.model.RoleToUserDTO;
import com.ajaxjs.spring.annotation.BizAction;
import com.ajaxjs.sqlman.page.PageResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 管理端接口
 */
@RestController
@RequestMapping("/admin")
public interface AdminController {
    @GetMapping("/welcome")
    Map<String, Object> welcome();

    /**
     * 分页获取用户角色。loginId 参数匹配账号或用户名，phone 和 email 支持模糊搜索，tenantId 用于精确筛选。
     *
     * @return 用户角色列表
     */
    @GetMapping("/user_role")
    @BizAction("获取用户角色")
    @PermissionCheck(modulePermissionCode = "IAM_ADMIN")
    PageResult<Map<String, Object>> userRoleList(HttpServletRequest req);

    /**
     * 为用户保存角色
     *
     * @param dto 提交的数据
     * @return 是否成功
     */
    @PostMapping("/role_to_user")
    @BizAction("为用户保存角色")
    boolean saveRoleToUser(@RequestBody RoleToUserDTO dto);
}
