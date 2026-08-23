package com.ajaxjs.oauth.impl.microsoft;

import com.ajaxjs.oauth.ApiProvider;

public class MicrosoftApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://login.microsoftonline.com/%s/oauth2/v2.0/authorize";
    }

    @Override
    public String accessToken() {
        return "https://login.microsoftonline.com/%s/oauth2/v2.0/token";
    }

    @Override
    public String userInfo() {
        return "https://graph.microsoft.com/v1.0/me";
    }

    @Override
    public String refresh() {
        return "https://login.microsoftonline.com/%s/oauth2/v2.0/token";
    }
}
