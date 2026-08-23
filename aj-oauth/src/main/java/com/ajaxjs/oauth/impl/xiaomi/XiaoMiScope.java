package com.ajaxjs.oauth.impl.xiaomi;

import com.ajaxjs.oauth.AuthScope;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum XiaoMiScope implements AuthScope {

    profile("user/profile", "获取用户的基本信息", true),
    OPENID("user/openIdV2", "获取用户的OpenID", true),
    PHONE_EMAIL("user/phoneAndEmail", "获取用户的手机号和邮箱", true);

    private final String scope;
    private final String description;
    private final boolean isDefault;

}
