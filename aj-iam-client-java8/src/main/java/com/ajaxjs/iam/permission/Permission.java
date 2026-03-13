package com.ajaxjs.iam.permission;

import lombok.Data;

/**
 * 权限
 */
@Data
public class Permission {
    /**
     * 权限 id
     */
    public Integer id;

    /**
     * 权限名称
     */
    public String name;

    /**
     * 权限简介
     */
    public String content;

    /**
     * 角色名称，在显示父级权限时候，有需要显示一下
     */
    public String roleName;

    /**
     * 是否继承权限
     */
    public Boolean isInherit;
}
