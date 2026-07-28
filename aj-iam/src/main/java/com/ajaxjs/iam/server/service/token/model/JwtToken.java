package com.ajaxjs.iam.server.service.token.model;

import lombok.Data;

@Data
public class JwtToken {
    private String token;

    private String tokenJson;

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
     * 是否是新注册的用户
     */
    private Boolean isNewlyUser = false;
}
