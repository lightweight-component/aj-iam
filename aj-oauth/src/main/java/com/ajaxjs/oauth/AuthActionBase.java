package com.ajaxjs.oauth;

import com.ajaxjs.oauth.model.*;
import com.ajaxjs.util.ObjectHelper;
import com.ajaxjs.util.httpremote.Post;
import lombok.Data;

import java.util.Map;

@Data
public abstract class AuthActionBase<T extends Token, C extends Callback, U extends AuthUser> implements AuthAction<T, C, U> {
    protected ApiProvider apiProvider;

    protected Config config;

    public AuthActionBase(ApiProvider apiProvider, Config config) {
        this.apiProvider = apiProvider;
        this.config = config;
    }

    @Override
    public String authorize(String state) {
        return apiProvider.authorize() + String.format(AUTHORIZE_PARAMS, config.getClientId(), config.getRedirectUri(), state);
    }

    private final static String AUTHORIZE_PARAMS = "?response_type=code&client_id=%s&redirect_uri=%s&state=%s";

    protected Map<String, String> getAccessTokenParams(String code) {
        Map<String, String> params = ObjectHelper.mapOf("client_id", config.getClientId(), "client_secret", config.getClientSecret());
        params.put("grant_type", "authorization_code");
        params.put("redirect_uri", config.getRedirectUri());
        params.put("code", code);

        return params;
    }

    protected Map<String, Object> getAccessTokenResult(Callback callback) {
        Map<String, String> params = getAccessTokenParams(callback.getCode());
        Map<String, Object> result = Post.api(getApiProvider().accessToken(), params);
        System.out.println(result);

        if (result.containsKey("error"))
            throw new AuthException((String) result.get("error_description"));

        return result;
    }


    /**
     * 检查响应内容是否正确
     *
     * @param result 请求响应内容
     */
    protected static void checkResponse(Map<String, Object> result) {
        if (result.containsKey("error"))
            throw new AuthException((String) result.get("error_description"));
    }
}
