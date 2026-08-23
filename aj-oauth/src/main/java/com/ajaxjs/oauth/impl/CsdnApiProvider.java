package com.ajaxjs.oauth.impl;

import com.ajaxjs.oauth.ApiProvider;

/**
 * CSDN 博客平台
 */
public class CsdnApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://api.csdn.net/oauth2/authorize";
    }

    @Override
    public String accessToken() {
        return "https://api.csdn.net/oauth2/access_token";
    }

    @Override
    public String userInfo() {
        return "https://api.csdn.net/user/getinfo";
    }
}
