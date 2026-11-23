package com.ajaxjs.iam.server.common.langs;

import java.util.Map;

public class Es {
    public final static Map<String, String> MAP = Map.of(
        "sms.phone.invalid", "Introduce un número de teléfono válido.",
        "sms.phone.provideAppInfo", "Proporciona información válida sobre la aplicación.",
        "sms.phone.invalid_verification_code", "Código de verificación no válido.",
        "sms.phone.error_verification_code", "Código de verificación incorrecto.",
        "sms.phone.phone_exist", "El usuario con el número de teléfono %s ya está registrado. Este número no se puede modificar.",
        "sms.phone.user_not_exist", "El usuario %s no existe."
    );
}
