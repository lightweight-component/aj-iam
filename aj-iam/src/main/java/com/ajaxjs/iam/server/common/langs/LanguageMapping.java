package com.ajaxjs.iam.server.common.langs;

import com.ajaxjs.framework.i18n.Internationalization;
import com.ajaxjs.framework.i18n.Language;
import com.ajaxjs.util.ObjectHelper;

import java.util.HashMap;
import java.util.Map;

public class LanguageMapping {
    public final static Map<Language, Map<String, String>> MAPPING = new HashMap<>();

    static {
        MAPPING.put(Language.ZH, Zh.MAP);
        MAPPING.put(Language.CH, Ch.MAP);
        MAPPING.put(Language.EN, En.MAP);
        MAPPING.put(Language.JA, Jp.MAP);
    }

    public static Map<String, String> getLangsMap() {
        Language language = Internationalization.getLanguage();
        System.out.println(language);
        Map<String, String> langs = MAPPING.get(Internationalization.getLanguage());
        System.out.println(langs);

        if (langs == null)
            throw new UnsupportedOperationException("Unsupported language.");

        return langs;
    }

    public static String getLanguageByKey(String key) {
        String msg = getLangsMap().get(key);

        return ObjectHelper.isEmptyText(msg) ? "Error when doing i18n of key:" + key : msg;
    }

    public static void main(String[] args) {
        System.out.println(getLanguageByKey("sms.phone.phone_exist"));
    }
}
