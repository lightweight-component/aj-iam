package com.ajaxjs.iam.server.common.langs;

import java.util.HashMap;
import java.util.Map;

public class En {
    public final static Map<String, String> MAP = new HashMap<>() {{
        put("sms.phone.invalid", "Please enter a valid phone number");
        put("sms.phone.provideAppInfo", "Please provide valid app information");
        put("sms.phone.invalid_verification_code", "Invalid verification code");
        put("sms.phone.error_verification_code", "Incorrect verification code");
        put("sms.phone.phone_exist", "The user with phone number %s is already registered. This phone number cannot be modified.");
    }};
}
