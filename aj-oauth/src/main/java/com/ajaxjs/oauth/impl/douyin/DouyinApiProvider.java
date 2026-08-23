package com.ajaxjs.oauth.impl.douyin;

import com.ajaxjs.oauth.ApiProvider;

public class DouyinApiProvider implements ApiProvider {
    public String authorize() {
        return "https://open.douyin.com/platform/oauth/connect";
    }

    @Override
    public String accessToken() {
        return "https://open.douyin.com/oauth/access_token/";
    }

    @Override
    public String userInfo() {
        return "https://open.douyin.com/oauth/userinfo/";
    }

    @Override
    public String refresh() {
        return "https://open.douyin.com/oauth/refresh_token/";
    }
}
