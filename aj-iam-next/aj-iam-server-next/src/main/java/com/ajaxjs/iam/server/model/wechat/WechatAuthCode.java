package com.ajaxjs.iam.server.model.wechat;

import lombok.Data;

@Data
public class WechatAuthCode {
    private String code;

    private String appId;
}
