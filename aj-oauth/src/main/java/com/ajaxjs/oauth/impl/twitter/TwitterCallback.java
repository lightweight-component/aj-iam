package com.ajaxjs.oauth.impl.twitter;

import com.ajaxjs.oauth.model.Callback;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class TwitterCallback extends Callback {
    /**
     * Twitter 回调后返回的 oauth_token
     */
    private String oauth_token;

    /**
     * Twitter 回调后返回的 oauth_verifier
     */
    private String oauth_verifier;
}
