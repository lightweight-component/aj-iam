package com.ajaxjs.iam.server.common.langs;

import java.util.Map;

public class Ru {
    public final static Map<String, String> MAP = Map.of(
        "sms.phone.invalid", "Введите действительный номер телефона.",
        "sms.phone.provideAppInfo", "Укажите корректные сведения о приложении.",
        "sms.phone.invalid_verification_code", "Недействительный код подтверждения.",
        "sms.phone.error_verification_code", "Неверный код подтверждения.",
        "sms.phone.phone_exist", "Пользователь с номером телефона %s уже зарегистрирован. Этот номер нельзя изменить.",
        "sms.phone.user_not_exist", "Пользователь %s не существует."
    );
}
