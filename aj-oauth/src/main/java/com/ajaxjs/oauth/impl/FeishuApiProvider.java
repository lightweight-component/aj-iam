package com.ajaxjs.oauth.impl;

import com.ajaxjs.oauth.ApiProvider;

/**
 * 飞书平台，企业自建应用授权登录，原逻辑由 beacon 集成于 1.14.0 版，但最新的飞书 api 已修改，并且飞书平台一直为 {@code Deprecated} 状态
 * <p>
 * 所以，最终修改该平台的实际发布版本为 1.15.9
 */
public class FeishuApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://open.feishu.cn/open-apis/authen/v1/index";
    }

    @Override
    public String accessToken() {
        return "https://open.feishu.cn/open-apis/authen/v1/access_token";
    }

    @Override
    public String userInfo() {
        return "https://open.feishu.cn/open-apis/authen/v1/user_info";
    }

    @Override
    public String refresh() {
        return "https://open.feishu.cn/open-apis/authen/v1/refresh_access_token";
    }
}
