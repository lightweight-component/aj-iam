package com.ajaxjs.iam.server.common.langs;

import java.util.HashMap;
import java.util.Map;

public class Jp {
    public final static Map<String, String> MAP = new HashMap<>() {{
        put("sms.phone.invalid", "有効な電話番号を入力してください");
        put("sms.phone.provideAppInfo", "有効なアプリ情報を入力してください");
        put("sms.phone.invalid_verification_code", "認証コードが無効です");
        put("sms.phone.error_verification_code", "認証コードが正しくありません");
        put("sms.phone.phone_exist", "電話番号 %s のユーザーは既に登録されています。この電話番号は変更できません。");
    }};
}
