package com.ajaxjs.iam.server.common.langs;

import java.util.Map;

public class Ms {
    public final static Map<String, String> MAP = Map.of(
        "sms.phone.invalid", "Sila masukkan nombor telefon yang sah.",
        "sms.phone.provideAppInfo", "Sila berikan maklumat aplikasi yang sah.",
        "sms.phone.invalid_verification_code", "Kod pengesahan tidak sah.",
        "sms.phone.error_verification_code", "Kod pengesahan tidak betul.",
        "sms.phone.phone_exist", "Pengguna dengan nombor telefon %s sudah berdaftar. Nombor telefon ini tidak boleh diubah.",
        "sms.phone.user_not_exist", "Pengguna %s tidak wujud."
    );
}
