package com.ajaxjs.iam.server.model.po;

import com.ajaxjs.framework.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 应用/客户端
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class App extends BaseModel {
    /**
     * 父级 id
     */
    private String pid;

    /**
     * 客户端 id
     */
    private String clientId;

    /**
     * 客户端秘钥
     */
    private String clientSecret;

    /**
     * 用户授权完成之后重定向回你的应用
     */
    private String redirectUri;

    /**
     * Token 有效期，单位：分钟
     */
    private Integer expires;

    /**
     * 应用类型：HTML， APP，API_SERVICE， RPC_SERVICE， MISC
     */
    private String type;

    /**
     * 图标
     */
    private String logo;

    /**
     * 登录页面的 URL 地址
     */
    private String loginPage;

    /**
     * 租户 id
     */
    private Integer tenantId;
}