package com.ajaxjs.oauth.impl.slack;

import com.ajaxjs.oauth.ApiProvider;


public class SlackApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://slack.com/oauth/v2/authorize";
    }

    /**
     * 该 API 获取到的是 access token
     * <a href="https://slack.com/api/oauth.token">...</a> 获取到的是 workspace token
     *
     * @return String
     */
    @Override
    public String accessToken() {
        return "https://slack.com/api/oauth.v2.access";
    }

    @Override
    public String userInfo() {
        return "https://slack.com/api/users.info";
    }

    @Override
    public String revoke() {
        return "https://slack.com/api/auth.revoke";
    }
}
