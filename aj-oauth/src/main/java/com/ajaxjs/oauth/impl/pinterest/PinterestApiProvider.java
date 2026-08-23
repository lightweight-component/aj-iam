package com.ajaxjs.oauth.impl.pinterest;

import com.ajaxjs.oauth.ApiProvider;

public class PinterestApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://api.pinterest.com/oauth";
    }

    @Override
    public String accessToken() {
        return "https://api.pinterest.com/v1/oauth/token";
    }

    @Override
    public String userInfo() {
        return "https://api.pinterest.com/v1/me";
    }
}
