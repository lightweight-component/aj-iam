package com.ajaxjs.iam.server.service.token.model;

import lombok.Data;

/**
 * 普通 Access Token
 */
@Data
public class AccessToken {
    /**
     * 普通 Access Token
     */
    private String accessToken;

    /**
     * 刷新 Token
     */
    private String refreshToken;

    /**
     * 有效期，以秒为单位
     */
    private Integer expiresIn;

    /**
     * RefreshToken 有效期，以秒为单位
     */
    private Integer refreshExpiresIn;

    /**
     * 权限范围
     */
    private String scope;
}
