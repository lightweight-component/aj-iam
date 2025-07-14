package com.ajaxjs.iam.user.model;

import com.ajaxjs.framework.model.BaseModel;
import com.ajaxjs.sqlman.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table("user_login_log")
@EqualsAndHashCode(callSuper = true)
public class LogLogin extends BaseModel {
    /**
     * 用户 id
     */
    private Long userId;

    private String userName;

    /**
     * 客户端标识
     */
    private String userAgent;

    /**
     * 是否登录后台
     */
    private Boolean adminLogin;

    private String ipLocation;

    /**
     * 数据字典：第三方类型
     */
    private Integer loginType;

    /**
     * 登录 ip
     */
    private String ip;
}