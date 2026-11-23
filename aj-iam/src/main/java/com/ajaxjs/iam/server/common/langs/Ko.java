package com.ajaxjs.iam.server.common.langs;

import java.util.Map;

public class Ko {
    public final static Map<String, String> MAP = Map.of(
        "sms.phone.invalid", "유효한 전화번호를 입력해 주세요.",
        "sms.phone.provideAppInfo", "유효한 앱 정보를 제공해 주세요.",
        "sms.phone.invalid_verification_code", "유효하지 않은 인증 코드입니다.",
        "sms.phone.error_verification_code", "인증 코드가 올바르지 않습니다.",
        "sms.phone.phone_exist", "전화번호 %s를 사용하는 사용자가 이미 등록되어 있습니다. 이 전화번호는 변경할 수 없습니다.",
        "sms.phone.user_not_exist", "사용자 %s이(가) 존재하지 않습니다."
    );
}
