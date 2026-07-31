package com.ajaxjs.iam.server.auth.apponekeylogin;

import com.ajaxjs.util.HashHelper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.ajaxjs.util.HashHelper.HMAC_SHA1;

public abstract class BaseAliOpenApi {
    static final String ENDPOINT = "https://dypnsapi.aliyuncs.com";

    static final String SIGNATURE_METHOD = "HMAC-SHA1";

    static final String SIGNATURE_VERSION = "1.0";

    /**
     * 构建 application/x-www-form-urlencoded 格式的请求体
     */
    public static String buildFormData(Map<String, String> params) {
        StringBuilder formData = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            formData.append("&")
                    .append(percentEncode(entry.getKey()))
                    .append("=")
                    .append(percentEncode(entry.getValue()));
        }

        if (!formData.isEmpty())
            formData.deleteCharAt(0); // Remove leading &

        return formData.toString();
    }

    /**
     * URL 编码函数，遵循阿里云规范 (使用 %2F 而非 %20)
     */
    public static String percentEncode(String value) {
        if (value == null)
            return "";

        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8)
                    .replace("+", "%20")
                    .replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 计算阿里云 OpenAPI 签名
     */
    static String calculateSignature(Map<String, String> params, String accessKeySecret) {
        StringBuilder canonicalizedQueryString = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            // 注意：签名计算时，Signature 参数本身不参与
            if ("Signature".equals(entry.getKey())) continue;

            canonicalizedQueryString.append("&")
                    .append(percentEncode(entry.getKey())).append("=")
                    .append(percentEncode(entry.getValue()));
        }

        if (!canonicalizedQueryString.isEmpty()) // 移除第一个 &
            canonicalizedQueryString.deleteCharAt(0);

        String stringToSign = "POST&" + percentEncode("/") + "&" + percentEncode(canonicalizedQueryString.toString());
        String key = accessKeySecret + "&"; // 阿里云要求在Secret后面加 '&'

        return new HashHelper(HMAC_SHA1, stringToSign).setKey(key).hashAsBase64(false);
    }
}
