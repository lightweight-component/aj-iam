package com.ajaxjs.oauth.new2.model;

import com.ajaxjs.sqlman.annotation.Table;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
@Table("oauth_provider")
public class Provider {
    private Long id;

    private String name;

    /**
     * The API of authorization.
     */
    private String authorizeApi;

    /**
     * This is the API used to get an access token.
     */
    private String accessTokenApi;

    /**
     * This is the API used to get the user information.
     */
    private String userInfoApi;

    /**
     * This is the API used to revoke authorization.
     */
    private String revokeApi;

    /**
     * This is the API used to refresh authorization.
     */
    private String refreshApi;

    /**
     * 客户端 id：对应各平台的 appKey
     */
    private String clientId;

    /**
     * 客户端 Secret：对应各平台的 appSecret
     */
    private String clientSecret;

    /**
     * 登录成功后的回调地址
     */
    private String redirectUri;

    /**
     * 扩展配置
     */
    private Map<String, Object> extConfig;

    private Integer stat;

    private String creator;

    private Date createDate;
}
