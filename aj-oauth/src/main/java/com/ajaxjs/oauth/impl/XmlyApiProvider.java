package com.ajaxjs.oauth.impl;

import com.ajaxjs.oauth.ApiProvider;

/**
 * 喜马拉雅
 */
public class XmlyApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://api.ximalaya.com/oauth2/js/authorize";
    }

    @Override
    public String accessToken() {
        return "https://api.ximalaya.com/oauth2/v2/access_token";
    }

    @Override
    public String userInfo() {
        return "https://api.ximalaya.com/profile/user_info";
    }

    @Override
    public String refresh() {
        return "https://oauth.aliyun.com/v1/token";
    }

}
