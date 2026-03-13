package com.ajaxjs.iam.jwtv2;

import lombok.Data;

/**
 * The payload part of JWT.
 */
@Data
public class Payload {
    /**
     * 主题
     */
    private String sub;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 受众
     */
    private String aud;

    /**
     * 过期时间
     */
    private Long exp;

    /**
     * 签发人
     */
    private String iss;

    /**
     * 签发时间
     */
    private Long iat;
}
