package com.ajaxjs.iam.server.common.langs;

import java.util.Map;

public class De {
    public final static Map<String, String> MAP = Map.of(
        "sms.phone.invalid", "Bitte geben Sie eine gültige Telefonnummer ein.",
        "sms.phone.provideAppInfo", "Bitte geben Sie gültige Informationen zur App an.",
        "sms.phone.invalid_verification_code", "Der Bestätigungscode ist ungültig.",
        "sms.phone.error_verification_code", "Der Bestätigungscode ist falsch.",
        "sms.phone.phone_exist", "Für die Telefonnummer %s ist bereits ein Benutzer registriert. Diese Telefonnummer kann nicht geändert werden.",
        "sms.phone.user_not_exist", "Der Benutzer %s existiert nicht."
    );
}
