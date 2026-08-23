package com.ajaxjs.oauth.impl.xiaomi;

import com.ajaxjs.oauth.ApiProvider;


public class XiaomiApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://account.xiaomi.com/oauth2/authorize";
    }

    @Override
    public String accessToken() {
        return "https://account.xiaomi.com/oauth2/token";
    }

    @Override
    public String userInfo() {
        return "https://open.account.xiaomi.com/user/profile";
    }

    @Override
    public String refresh() {
        return "https://account.xiaomi.com/oauth2/token";
    }
}
