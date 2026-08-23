package com.ajaxjs.oauth.impl;

import com.ajaxjs.oauth.ApiProvider;

/**
 * 微软中国
 */
public class MicrosoftCnApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://login.partner.microsoftonline.cn/%s/oauth2/v2.0/authorize";
    }

    @Override
    public String accessToken() {
        return "https://login.partner.microsoftonline.cn/%s/oauth2/v2.0/token";
    }

    @Override
    public String userInfo() {
        return "https://microsoftgraph.chinacloudapi.cn/v1.0/me";
    }

    @Override
    public String refresh() {
        return "https://login.partner.microsoftonline.cn/%s/oauth2/v2.0/token";
    }

}
