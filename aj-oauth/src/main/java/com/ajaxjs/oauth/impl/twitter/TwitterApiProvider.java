package com.ajaxjs.oauth.impl.twitter;

import com.ajaxjs.oauth.ApiProvider;

public class TwitterApiProvider implements ApiProvider {
    public String authorize() {
        return "https://api.twitter.com/oauth/authenticate";
    }

    @Override
    public String accessToken() {
        return "https://api.twitter.com/oauth/access_token";
    }

    @Override
    public String userInfo() {
        return "https://api.twitter.com/1.1/account/verify_credentials.json";
    }
}
