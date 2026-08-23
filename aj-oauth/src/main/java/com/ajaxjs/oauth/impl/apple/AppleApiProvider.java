package com.ajaxjs.oauth.impl.apple;

import com.ajaxjs.oauth.ApiProvider;

public class AppleApiProvider implements ApiProvider {
    @Override
    public String authorize() {
        return "https://appleid.apple.com/auth/authorize";
    }

    /**
     * @see <a href="https://developer.apple.com/documentation/sign_in_with_apple/generate_and_validate_tokens">generate_and_validate_tokens</a>
     */
    @Override
    public String accessToken() {
        return "https://appleid.apple.com/auth/token";
    }

    @Override
    public String userInfo() {
        return "";
    }
}
