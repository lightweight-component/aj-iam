package com.ajaxjs.oauth.impl.huawei;

import com.ajaxjs.oauth.ApiProvider;


public class HuaweiApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://oauth-login.cloud.huawei.com/oauth2/v3/authorize";
    }

    @Override
    public String accessToken() {
        return "https://oauth-login.cloud.huawei.com/oauth2/v3/token";
    }

    @Override
    public String userInfo() {
        return "https://account.cloud.huawei.com/rest.php";
    }

    @Override
    public String refresh() {
        return "https://oauth-login.cloud.huawei.com/oauth2/v3/token";
    }

}
