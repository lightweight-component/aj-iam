package com.ajaxjs.iam.server.common.langs;

import java.util.Map;

public class Fr {
    public final static Map<String, String> MAP = Map.of(
        "sms.phone.invalid", "Veuillez saisir un numéro de téléphone valide.",
        "sms.phone.provideAppInfo", "Veuillez fournir des informations valides sur l’application.",
        "sms.phone.invalid_verification_code", "Code de vérification non valide.",
        "sms.phone.error_verification_code", "Code de vérification incorrect.",
        "sms.phone.phone_exist", "L’utilisateur associé au numéro de téléphone %s est déjà inscrit. Ce numéro ne peut pas être modifié.",
        "sms.phone.user_not_exist", "L’utilisateur %s n’existe pas."
    );
}
