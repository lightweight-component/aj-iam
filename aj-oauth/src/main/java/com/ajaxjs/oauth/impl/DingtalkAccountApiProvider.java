package com.ajaxjs.oauth.impl;

import com.ajaxjs.oauth.ApiProvider;

/**
 * 钉钉账号登录
 */
public class DingtalkAccountApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://oapi.dingtalk.com/connect/oauth2/sns_authorize";
    }

    @Override
    public String accessToken() {
        return new DingtalkApiProvider().accessToken();
    }

    @Override
    public String userInfo() {
        return new DingtalkApiProvider().userInfo();
    }
}
