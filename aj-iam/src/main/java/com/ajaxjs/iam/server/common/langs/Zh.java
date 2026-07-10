package com.ajaxjs.iam.server.common.langs;

import java.util.HashMap;
import java.util.Map;

public class Zh {
    public final static Map<String, String> MAP = new HashMap<>() {{
        put("sms.phone.invalid", "请提交有效的手机");
        put("sms.phone.provideAppInfo", "请提供有效的 App 信息");
        put("sms.phone.invalid_verification_code", "验证码非法");
        put("sms.phone.error_verification_code", "验证码错误 ");
        put("sms.phone.phone_exist", "当前手机 %s 的用户已经注册。不支持修改该手机号码。");
    }};
}
