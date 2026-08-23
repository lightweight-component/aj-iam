package com.ajaxjs.oauth.impl.line;

import com.ajaxjs.oauth.ApiProvider;

/**
 * 飞书平台，企业自建应用授权登录，原逻辑由 beacon 集成于 1.14.0 版，但最新的飞书 api 已修改，并且飞书平台一直为 {@code Deprecated} 状态
 * <p>
 * 所以，最终修改该平台的实际发布版本为 1.15.9
 */
public class LineApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://access.line.me/oauth2/v2.1/authorize";
    }

    @Override
    public String accessToken() {
        return "https://api.line.me/oauth2/v2.1/token";
    }

    @Override
    public String userInfo() {
        return "https://api.line.me/v2/profile";
    }

    @Override
    public String refresh() {
        return "https://api.line.me/oauth2/v2.1/token";
    }

    @Override
    public String revoke() {
        return "https://api.line.me/oauth2/v2.1/revoke";
    }

}
