package com.ajaxjs.iam.server.model.wechat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MiniAppPhoneNumber {
    String phoneNumber;

    String purePhoneNumber;

    String countryCode;

    /**
     * 是否新注册的用户
     */
    Boolean isNewlyUser = false;
}
