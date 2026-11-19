package com.ajaxjs.iam.server.auth.model.wechat;

import lombok.Data;

@Data
public class PhoneNumberLoginDTO {
    String data;

    String iv;

    String code;

    String appId;
}
