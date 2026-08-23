package com.ajaxjs.oauth.impl;

import com.ajaxjs.oauth.ApiProvider;

/**
 * 饿了么
 */
public class ElemeApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://open-api.shop.ele.me/authorize";
    }

    @Override
    public String accessToken() {
        return "https://open-api.shop.ele.me/token";
    }

    @Override
    public String userInfo() {
        return "https://open-api.shop.ele.me/api/v1/";
    }

    @Override
    public String refresh() {
        return "https://open-api.shop.ele.me/token";
    }

}
