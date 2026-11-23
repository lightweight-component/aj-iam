package com.ajaxjs.iam.server.common.langs;

import java.util.Map;

public class Pt {
    public final static Map<String, String> MAP = Map.of(
        "sms.phone.invalid", "Insira um número de telefone válido.",
        "sms.phone.provideAppInfo", "Forneça informações válidas sobre a aplicação.",
        "sms.phone.invalid_verification_code", "Código de verificação inválido.",
        "sms.phone.error_verification_code", "Código de verificação incorreto.",
        "sms.phone.phone_exist", "O utilizador com o número de telefone %s já está registado. Este número não pode ser alterado.",
        "sms.phone.user_not_exist", "O utilizador %s não existe."
    );
}
