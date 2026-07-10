package com.ajaxjs.iam.server.common.langs;

import java.util.HashMap;
import java.util.Map;

public class Ch {
    public final static Map<String, String> MAP = new HashMap<>() {{
        put("sms.phone.invalid", "請提交有效的手機");
        put("sms.phone.provideAppInfo", "請提供有效的 App 資訊");
        put("sms.phone.invalid_verification_code", "驗證碼非法");
        put("sms.phone.error_verification_code", "驗證碼錯誤 ");
        put("sms.phone.phone_exist", "當前手機 %s 的使用者已經註冊。不支援修改該手機號碼。");
    }};
}
