package com.ajaxjs.oauth.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class Token {
    private String accessToken;

    private int expireIn;

    private String refreshToken;

    private int refreshTokenExpireIn;

    private String uid;

    private String openId;

    private String accessCode;

    private String unionId;
}
