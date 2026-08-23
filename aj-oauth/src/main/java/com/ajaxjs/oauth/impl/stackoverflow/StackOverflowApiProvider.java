package com.ajaxjs.oauth.impl.stackoverflow;

import com.ajaxjs.oauth.ApiProvider;


public class StackOverflowApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://stackoverflow.com/oauth";
    }

    @Override
    public String accessToken() {
        return "https://stackoverflow.com/oauth/access_token/json";
    }

    @Override
    public String userInfo() {
        return "https://api.stackexchange.com/2.2/me";
    }

}
