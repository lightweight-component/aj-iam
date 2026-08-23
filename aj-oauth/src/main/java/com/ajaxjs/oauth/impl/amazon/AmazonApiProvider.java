package com.ajaxjs.oauth.impl.amazon;

import com.ajaxjs.oauth.ApiProvider;

public class AmazonApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://www.amazon.com/ap/oa";
    }

    @Override
    public String accessToken() {
        return "https://api.amazon.com/auth/o2/token";
    }

    @Override
    public String userInfo() {
        return "https://api.amazon.com/user/profile";
    }

    @Override
    public String refresh() {
        return "https://api.amazon.com/auth/o2/token";
    }
}
