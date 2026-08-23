package com.ajaxjs.oauth.impl.alipay;

import com.ajaxjs.oauth.model.Config;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class AlipayConfig extends Config {
    /**
     * 支付宝公钥：当选择支付宝登录时，该值可用
     * 对应“RSA2(SHA256)密钥”中的“支付宝公钥”
     *
     * @deprecated 请使用AuthAlipayRequest的构造方法设置"alipayPublicKey"
     */
    @Deprecated
    private String alipayPublicKey;
}
