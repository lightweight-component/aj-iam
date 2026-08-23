package com.ajaxjs.oauth.impl.baidu;

import com.ajaxjs.oauth.ApiProvider;

public class BaiduApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://openapi.baidu.com/oauth/2.0/authorize";
    }

    @Override
    public String accessToken() {
        return "https://openapi.baidu.com/oauth/2.0/token";
    }

    @Override
    public String userInfo() {
        return "https://openapi.baidu.com/rest/2.0/passport/users/getInfo";
    }

    @Override
    public String revoke() {
        return "https://openapi.baidu.com/rest/2.0/passport/auth/revokeAuthorization";
    }

    @Override
    public String refresh() {
        return "https://openapi.baidu.com/oauth/2.0/token";
    }

}
