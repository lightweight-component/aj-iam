package com.ajaxjs.oauth.impl.jd;

import com.ajaxjs.oauth.ApiProvider;

public class JdApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://open-oauth.jd.com/oauth2/to_login";
    }

    @Override
    public String accessToken() {
        return "https://open-oauth.jd.com/oauth2/access_token";
    }

    @Override
    public String userInfo() {
        return "https://api.jd.com/routerjson";
    }

    @Override
    public String refresh() {
        return "https://open-oauth.jd.com/oauth2/refresh_token";
    }
}
