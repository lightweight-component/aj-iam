package com.ajaxjs.oauth.impl.weixin.wechatmp;

import com.ajaxjs.oauth.ApiProvider;

/**
 * 微信公众平台
 */
public class WechatMpApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://open.weixin.qq.com/connect/oauth2/authorize";
    }

    @Override
    public String accessToken() {
        return "https://api.weixin.qq.com/sns/oauth2/access_token";
    }

    @Override
    public String userInfo() {
        return "https://api.weixin.qq.com/sns/userinfo";
    }

    @Override
    public String refresh() {
        return "https://api.weixin.qq.com/sns/oauth2/refresh_token";
    }
}
