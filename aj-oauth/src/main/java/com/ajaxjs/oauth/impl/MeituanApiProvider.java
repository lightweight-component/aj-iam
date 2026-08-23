package com.ajaxjs.oauth.impl;

import com.ajaxjs.oauth.ApiProvider;

/**
 * 美团外卖
 */
public class MeituanApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://openapi.waimai.meituan.com/oauth/authorize";
    }

    @Override
    public String accessToken() {
        return "https://openapi.waimai.meituan.com/oauth/access_token";
    }

    @Override
    public String userInfo() {
        return "https://openapi.waimai.meituan.com/oauth/userinfo";
    }

    @Override
    public String refresh() {
        return "https://openapi.waimai.meituan.com/oauth/refresh_token";
    }
}
