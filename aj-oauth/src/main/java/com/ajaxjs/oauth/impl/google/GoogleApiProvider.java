package com.ajaxjs.oauth.impl.google;

import com.ajaxjs.oauth.ApiProvider;

/**
 * Google
 * 端点地址：<a href="https://accounts.google.com/.well-known/openid-configuration">...</a>
 */
public class GoogleApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://accounts.google.com/o/oauth2/v2/auth";
    }

    @Override
    public String accessToken() {
        return "https://oauth2.googleapis.com/token";
    }

    @Override
    public String userInfo() {
        return "https://openidconnect.googleapis.com/v1/userinfo";
    }
}
