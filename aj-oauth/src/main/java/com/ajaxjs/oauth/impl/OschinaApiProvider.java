package com.ajaxjs.oauth.impl;

import com.ajaxjs.oauth.ApiProvider;

/**
 * 开源中国
 */
public class OschinaApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://www.oschina.net/action/oauth2/authorize";
    }

    @Override
    public String accessToken() {
        return "https://www.oschina.net/action/openapi/token";
    }

    @Override
    public String userInfo() {
        return "https://www.oschina.net/action/openapi/user";
    }

}
