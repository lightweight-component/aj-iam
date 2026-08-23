package com.ajaxjs.oauth.impl;

import com.ajaxjs.oauth.ApiProvider;
import com.ajaxjs.oauth.model.AuthException;
import com.ajaxjs.oauth.model.ResponseStatus;

/**
 * 淘宝
 */
public class TaobaoApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://oauth.taobao.com/authorize";
    }

    @Override
    public String accessToken() {
        return "https://oauth.taobao.com/token";
    }

    @Override
    public String userInfo() {
        throw new AuthException(ResponseStatus.UNSUPPORTED);
    }

}
