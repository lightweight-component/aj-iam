package com.ajaxjs.iam.server.model.po;

import com.ajaxjs.sqlman.annotation.Id;
import com.ajaxjs.sqlman.annotation.Table;
import lombok.Data;

import java.util.Date;

@Data
@Table("access_token")
@Id("id")
public class AccessTokenPo {
    /**
     * 主键 id，自增
     */
    private Long id;

    /**
     * Access Token
     */
    private String accessToken;

    /**
     * Refresh Token
     */
    private String refreshToken;

    /**
     * JWT Token JSON 格式，以便复用
     */
    private String jwtToken;

    /**
     * 关联的用户 id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 接入的客户端ID
     */
    private String clientId;

    /**
     * 过期时间
     */
    private Date expiresDate;

    /**
     * 授权类型，比如：authorization_code
     */
    private String grantType;

    /**
     * 可被访问的用户的权限范围，比如：basic、super
     */
    private String scope;

    /**
     * 创建人名称（可冗余的） COLLATE utf8mb4_bin
     */
    private String creator;

    /**
     * 创建人 id
     */
    private Long creatorId;

    /**
     * 创建日期
     */
    private Date createDate;
}
