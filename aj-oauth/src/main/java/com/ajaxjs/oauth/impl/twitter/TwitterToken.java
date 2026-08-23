package com.ajaxjs.oauth.impl.twitter;

import com.ajaxjs.oauth.model.Token;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * Twitter 附带属性
 */
@Data
@SuperBuilder
public class TwitterToken extends Token {
    private String oauthToken;

    private String oauthTokenSecret;

    private String userId;

    private String screenName;

    private Boolean oauthCallbackConfirmed;
}
