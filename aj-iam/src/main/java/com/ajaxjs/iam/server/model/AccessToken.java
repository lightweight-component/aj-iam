package com.ajaxjs.iam.server.model;

import lombok.Data;

/**
 * 访问令牌
 */
@Data
public class AccessToken {
    /**
     * 普通 Access Token，UUID 生成，如果用了 JWT 这个就没什么用了。但保留吧
     */
    private String access_token;

    /**
     * 刷新 Token
     */
    private String refresh_token;

    /**
     * 有效期，以秒为单位
     */
    private Integer expires_in;

    /**
     * 权限范围
     */
    private String scope;
}
