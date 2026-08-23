package com.ajaxjs.oauth.impl;

import com.ajaxjs.oauth.ApiProvider;

/**
 * 阿里云
 */
public class AliyunApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://signin.aliyun.com/oauth2/v1/auth";
    }

    @Override
    public String accessToken() {
        return "https://oauth.aliyun.com/v1/token";
    }

    @Override
    public String userInfo() {
        return "https://oauth.aliyun.com/v1/userinfo";
    }

    @Override
    public String refresh() {
        return "https://oauth.aliyun.com/v1/token";
    }
}
