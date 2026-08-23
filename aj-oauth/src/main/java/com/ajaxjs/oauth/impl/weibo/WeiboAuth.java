package com.ajaxjs.oauth.impl.weibo;

import com.ajaxjs.oauth.AuthActionBase;
import com.ajaxjs.oauth.model.*;
import com.ajaxjs.util.ObjectHelper;
import com.ajaxjs.util.httpremote.Get;

import java.util.Map;

public class WeiboAuth extends AuthActionBase<TokenDefault, CallbackDefault, AuthUserDefault> {
    public WeiboAuth(Config config) {
        super(new WeiboApiProvider(), config);
    }

    @Override
    public TokenDefault getAccessToken(CallbackDefault callback) {
        Map<String, Object> result = getAccessTokenResult(callback);

//        TokenDefault token = new TokenDefault();
//        token.setAccessToken((String) result.get("access_token"));
//        token.setUid((String) result.get("uid"));
//        token.setOpenId((String) result.get("uid"));
//        token.setExpireIn(Integer.parseInt(result.get("expires_in").toString()));

        return TokenDefault.builder().build();
    }

    @Override
    public AuthUserDefault getUserInfo(TokenDefault token) {
        String url = getApiProvider().userInfo() + "?uid=" + token.getUid() + "&access_token=" + token.getAccessToken();
        Map<String, Object> userInfo = Get.api(url);
        System.out.println(userInfo);

        if (userInfo.containsKey("error"))
            throw new AuthException((String) userInfo.get("error"));

        return AuthUserDefault.builder()
                .rawUserInfo(userInfo)
                .uuid((String) userInfo.get("id"))
                .username((String) userInfo.get("name"))
                .avatar((String) userInfo.get("profile_image_url"))
                .blog(ObjectHelper.isEmptyText((String) userInfo.get("url")) ? "https://weibo.com/" + (String) userInfo.get("profile_url") : (String) userInfo.get("url"))
                .nickname((String) userInfo.get("screen_name"))
                .location((String) userInfo.get("location"))
                .remark((String) userInfo.get("description"))
                .gender(UserGender.getRealGender((String) userInfo.get("gender")))
                .source("weibo")
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
