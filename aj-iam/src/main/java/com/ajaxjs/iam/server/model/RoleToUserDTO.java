package com.ajaxjs.iam.server.model;

import lombok.Data;

import java.util.List;

/**
 * 为用户保存角色
 */
@Data
public class RoleToUserDTO {
    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 保存后的可编辑角色 ID 列表，空数组清空可编辑角色。
     */
    private List<Long> roleIds;
}
