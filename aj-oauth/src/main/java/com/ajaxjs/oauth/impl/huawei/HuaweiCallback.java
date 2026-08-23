package com.ajaxjs.oauth.impl.huawei;

import com.ajaxjs.oauth.model.Callback;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class HuaweiCallback extends Callback {
    /**
     * 华为授权登录接受 code 的参数名
     */
    private String authorization_code;
}
