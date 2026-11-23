package com.ajaxjs.iam.server.common.langs;

import com.ajaxjs.framework.i18n.Language;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LanguageMappingTest {
    @Test
    void everyLanguageHasAllMessages() {
        assertEquals(EnumSet.allOf(Language.class), LanguageMapping.MAPPING.keySet());

        LanguageMapping.MAPPING.forEach((language, messages) -> {
            assertEquals(En.MAP.keySet(), messages.keySet(), language.name());
            messages.forEach((key, message) -> assertFalse(message.isBlank(), language + ": " + key));
        });
    }

    @Test
    void phoneMessagesInterpolateTheirArgument() {
        String phone = "+1234567890";

        LanguageMapping.MAPPING.forEach((language, messages) -> {
            for (String key : new String[]{"sms.phone.phone_exist", "sms.phone.user_not_exist"}) {
                String formatted = String.format(Locale.ROOT, messages.get(key), phone);
                assertTrue(formatted.contains(phone), language + ": " + key);
            }
        });
    }
}
