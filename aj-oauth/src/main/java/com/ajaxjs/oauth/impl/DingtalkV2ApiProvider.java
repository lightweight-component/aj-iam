package com.ajaxjs.oauth.impl;

import com.ajaxjs.oauth.ApiProvider;

/**
 * 新版钉钉扫码登录
 */
public class DingtalkV2ApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://login.dingtalk.com/oauth2/challenge.htm";
    }

    @Override
    public String accessToken() {
        return "https://api.dingtalk.com/v1.0/oauth2/userAccessToken";
    }

    @Override
    public String userInfo() {
        return "https://api.dingtalk.com/v1.0/contact/users/me";
    }
}
