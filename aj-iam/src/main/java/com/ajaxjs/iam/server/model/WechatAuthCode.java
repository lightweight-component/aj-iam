package com.ajaxjs.iam.server.model;

import lombok.Data;

@Data
public class WechatAuthCode {
    private String code;

    private String appId;
}
