package com.ajaxjs.oauth.impl.gitee;

import com.ajaxjs.oauth.ApiProvider;

public class GiteeApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://gitee.com/oauth/authorize";
    }

    @Override
    public String accessToken() {
        return "https://gitee.com/oauth/token";
    }

    @Override
    public String userInfo() {
        return "https://gitee.com/api/v5/user";
    }
}
