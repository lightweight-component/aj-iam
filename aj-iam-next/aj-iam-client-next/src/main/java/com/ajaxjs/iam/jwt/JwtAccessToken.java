package com.ajaxjs.iam.jwt;

import com.ajaxjs.iam.model.AccessToken;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class JwtAccessToken extends AccessToken {
    /**
     * The type of the token
     */
    private String token_type = "Bearer";

    /**
     * JWT Token encoded.
     */
    private String id_token;

    /**
     * 是否是新注册的用户
     */
    private Boolean isNewlyUser = false;
}
