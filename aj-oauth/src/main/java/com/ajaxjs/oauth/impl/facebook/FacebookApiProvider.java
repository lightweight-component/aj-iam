package com.ajaxjs.oauth.impl.facebook;

import com.ajaxjs.oauth.ApiProvider;

public class FacebookApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://www.facebook.com/v18.0/dialog/oauth";
    }

    @Override
    public String accessToken() {
        return "https://graph.facebook.com/v18.0/oauth/access_token";
    }

    @Override
    public String userInfo() {
        return "https://graph.facebook.com/v18.0/me";
    }
}
