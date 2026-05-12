package com.ajaxjs.iam.server.model;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 应用密钥管理器
 */
@Data
public class AppSecretMgr {
    /**
     * 主键 id，自增
     */
    private Long id;

    /**
     * 说明
     */
    private String content;

    /**
     * 客户端 id
     */
    private String appId;

    /**
     * 客户端秘钥
     */
    private String appSecret;

    /**
     * 所属于实体的 id，可以是 AppId 或其他实体的 id
     */
    private String owner;

    /**
     * 数据字典：状态
     */
    private Integer stat;

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
    private LocalDateTime createDate;

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
    private LocalDateTime updateDate;
}