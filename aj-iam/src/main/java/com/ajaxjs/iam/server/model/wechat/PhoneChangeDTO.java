package com.ajaxjs.iam.server.model.wechat;

import lombok.Data;

/**
 * 修改电话号码
 */
@Data
public class PhoneChangeDTO {
    String iv;

    String cipherText;
}
