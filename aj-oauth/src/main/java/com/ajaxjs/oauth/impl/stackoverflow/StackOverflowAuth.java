package com.ajaxjs.oauth.impl.stackoverflow;

import com.ajaxjs.oauth.AuthActionBase;
import com.ajaxjs.oauth.model.*;
import com.ajaxjs.util.httpremote.Get;

import java.util.List;
import java.util.Map;

public class StackOverflowAuth extends AuthActionBase<TokenDefault, CallbackDefault, AuthUserDefault> {
    private final StackOverflowConfig stackOverflowConfig;

    public StackOverflowAuth(StackOverflowConfig config) {
        super(new StackOverflowApiProvider(), config);
        this.stackOverflowConfig = config;
    }

    @Override
    public TokenDefault getAccessToken(CallbackDefault callback) {
        Map<String, Object> result = getAccessTokenResult(callback);

        checkResponse(result);

        return TokenDefault.builder()
                .accessToken((String) result.get("access_token"))
                .expireIn((int) result.get("expires"))
                .build();
    }

    @Override
    public AuthUserDefault getUserInfo(TokenDefault token) {
        String url = getApiProvider().userInfo() + "?site=stackoverflow&key=" + stackOverflowConfig.getStackOverflowKey() + "&access_token=" + token.getAccessToken();
        Map<String, Object> userInfo = Get.api(url);

        checkResponse(userInfo);

        List<Map<String, Object>> items = (List<Map<String, Object>>) userInfo.get("items");
        Map<String, Object> userObj = items.get(0);

        return AuthUserDefault.builder()
                .rawUserInfo(userObj)
                .uuid((String) userObj.get("user_id"))
                .avatar((String) userObj.get("profile_image"))
                .location((String) userObj.get("location"))
                .nickname((String) userObj.get("display_name"))
                .blog((String) userObj.get("website_url"))
                .gender(UserGender.UNKNOWN)
                .source("StackOverflow")
                .build();
    }

    @Override
    public AuthResponse<Boolean> revoke(TokenDefault token) {
        String url = getApiProvider().revoke() + "?access_token=" + token.getAccessToken();
        Map<String, Object> result = Get.api(url);

        if (result.containsKey("error"))
            return AuthResponse.<Boolean>builder()
                    .code(ResponseStatus.FAILURE.getCode())
                    .msg((String) result.get("error"))
                    .data(false)
                    .build();

        // 返回 result = true 表示取消授权成功，否则失败
        ResponseStatus status = Boolean.parseBoolean(result.get("result").toString()) ? ResponseStatus.SUCCESS : ResponseStatus.FAILURE;

        return AuthResponse.<Boolean>builder().code(status.getCode()).msg(status.getMsg()).data(true).build();
    }
}
