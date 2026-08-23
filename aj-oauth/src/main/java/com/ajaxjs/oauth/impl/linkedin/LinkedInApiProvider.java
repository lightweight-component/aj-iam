package com.ajaxjs.oauth.impl.linkedin;

import com.ajaxjs.oauth.ApiProvider;

public class LinkedInApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://www.linkedin.com/oauth/v2/authorization";
    }

    @Override
    public String accessToken() {
        return "https://www.linkedin.com/oauth/v2/accessToken";
    }

    @Override
    public String userInfo() {
        return "https://api.linkedin.com/v2/me";
    }

    @Override
    public String refresh() {
        return "https://www.linkedin.com/oauth/v2/accessToken";
    }
}
