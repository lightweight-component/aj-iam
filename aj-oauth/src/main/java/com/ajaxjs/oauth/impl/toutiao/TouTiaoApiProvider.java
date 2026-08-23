package com.ajaxjs.oauth.impl.toutiao;

import com.ajaxjs.oauth.ApiProvider;

public class TouTiaoApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://open.snssdk.com/auth/authorize";
    }

    @Override
    public String accessToken() {
        return "https://open.snssdk.com/auth/token";
    }

    @Override
    public String userInfo() {
        return "https://open.snssdk.com/data/user_profile";
    }

}
