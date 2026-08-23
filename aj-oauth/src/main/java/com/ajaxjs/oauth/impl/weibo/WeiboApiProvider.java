package com.ajaxjs.oauth.impl.weibo;

import com.ajaxjs.oauth.ApiProvider;

public class WeiboApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://api.weibo.com/oauth2/authorize";
    }

    @Override
    public String accessToken() {
        return "https://api.weibo.com/oauth2/access_token";
    }

    @Override
    public String userInfo() {
        return "https://api.weibo.com/2/users/show.json";
    }

    @Override
    public String revoke() {
        return "https://api.weibo.com/oauth2/revokeoauth2";
    }
}
