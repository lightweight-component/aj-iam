package com.ajaxjs.oauth.impl.weixin.wechat_enterprise;

import com.ajaxjs.oauth.ApiProvider;

/**
 * 企业微信二维码登录
 */
public class WechatEnterpriseApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://open.work.weixin.qq.com/wwopen/sso/qrConnect";
    }

    @Override
    public String accessToken() {
        return "https://qyapi.weixin.qq.com/cgi-bin/gettoken";
    }

    @Override
    public String userInfo() {
        return "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo";
    }
}
