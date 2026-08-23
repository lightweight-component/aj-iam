package com.ajaxjs.oauth.impl.qq;

import com.ajaxjs.oauth.ApiProvider;

public class QqApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://graph.qq.com/oauth2.0/authorize";
    }

    @Override
    public String accessToken() {
        return "https://graph.qq.com/oauth2.0/token";
    }

    @Override
    public String userInfo() {
        return "https://graph.qq.com/user/get_user_info";
    }

    @Override
    public String refresh() {
        return "https://graph.qq.com/oauth2.0/token";
    }
}
