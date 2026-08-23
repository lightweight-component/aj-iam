package com.ajaxjs.oauth.impl.alipay;

import com.ajaxjs.oauth.ApiProvider;

public class AlipayApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm";
    }

    @Override
    public String accessToken() {
        return "https://openapi.alipay.com/gateway.do";
    }

    @Override
    public String userInfo() {
        return "https://openapi.alipay.com/gateway.do";
    }

}
