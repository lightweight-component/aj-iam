package com.ajaxjs.iam.server.common.langs;

import java.util.Map;

public class Ar {
    public final static Map<String, String> MAP = Map.of(
        "sms.phone.invalid", "يرجى إدخال رقم هاتف صالح.",
        "sms.phone.provideAppInfo", "يرجى تقديم معلومات صالحة عن التطبيق.",
        "sms.phone.invalid_verification_code", "رمز التحقق غير صالح.",
        "sms.phone.error_verification_code", "رمز التحقق غير صحيح.",
        "sms.phone.phone_exist", "المستخدم صاحب رقم الهاتف %s مسجل بالفعل. لا يمكن تعديل رقم الهاتف هذا.",
        "sms.phone.user_not_exist", "المستخدم %s غير موجود."
    );
}
