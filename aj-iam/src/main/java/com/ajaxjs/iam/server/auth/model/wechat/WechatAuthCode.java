package com.ajaxjs.iam.server.auth.model.wechat;

import lombok.Data;

@Data
public class WechatAuthCode {
    private String code;

    private String appId;
}
