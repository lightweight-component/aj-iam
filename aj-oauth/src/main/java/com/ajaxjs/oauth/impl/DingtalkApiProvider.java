package com.ajaxjs.oauth.impl;

import com.ajaxjs.oauth.ApiProvider;
import com.ajaxjs.oauth.model.AuthException;
import com.ajaxjs.oauth.model.ResponseStatus;

/**
 * 钉钉扫码登录
 */
public class DingtalkApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://oapi.dingtalk.com/connect/qrconnect";
    }

    @Override
    public String accessToken() {
        throw new AuthException(ResponseStatus.UNSUPPORTED);
    }

    @Override
    public String userInfo() {
        return "https://oapi.dingtalk.com/sns/getuserinfo_bycode";
    }
}
