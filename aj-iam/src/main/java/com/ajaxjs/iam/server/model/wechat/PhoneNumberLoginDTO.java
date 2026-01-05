package com.ajaxjs.iam.server.model.wechat;

import lombok.Data;

@Data
public class PhoneNumberLoginDTO {
    String data;

    String iv;

    String code;

    String appId;
}
