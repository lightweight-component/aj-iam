package com.ajaxjs.iam.model;

import lombok.Data;

import java.util.Date;

/**
 * 數據庫對應的用戶
 */
@Data
public class UserEntity {
    /**
     * 主键 id，自增
     */
    private Long id;

    /**
     * 唯一不重复 id，可以是雪花 id
     */
    private Long uid;

    /**
     * 部门 ID
     */
    private Integer orgId;

    /**
     * 租户 id
     */
    private Integer tenantId;

    /**
     * 用户登录 id，不可重复
     */
    private String loginId;

    /**
     * 用户名称，可以重复
     */
    private String username;

    /**
     * 用户真实姓名
     */
    private String realName;

    /**
     * 备注
     */
    private String content;

    /**
     * 性别
     */
    private int gender;

    /**
     * 出生日期
     */
    private Date birthday;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 数据字典：状态
     */
    private int stat;

    /**
     * 扩展 JSON 字段
     */
    private String extend;

    /**
     * 创建人名称（可冗余的）
     */
    private String creator;

    /**
     * 创建人 id
     */
    private Integer creatorId;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 修改人名称（可冗余的）
     */
    private String updater;

    /**
     * 修改人 id
     */
    private Integer updaterId;

    /**
     * 修改日期
     */
    private Date updateDate;
}
