package com.ajaxjs.iam.jwt;

import com.ajaxjs.util.Base64Utils;
import com.ajaxjs.util.HashHelper;
import com.ajaxjs.util.ObjectHelper;
import lombok.Data;

/**
 * JWT Token using HS256 algorithm.
 * The structure of JWT looks like: Header.Payload.Signature, separated by dots.
 */
@Data
public class JWebToken {
    /**
     * The header part of JWT.
     * It is encoded as Base64, the original string is "{\"alg\":\"HS256\",\"typ\":\"JWT\"}".
     */
    public static final String HEADER = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";

    /**
     * The payload（载荷） part of JWT.
     */
    private Payload payload;

    /**
     * 载荷(json)
     * <p>
     * 不同的 json 反序列化，对 key 顺序不一致。所以直接拿 json 字符串加密
     */
    private String payloadJson;

    /**
     * The signature part of JWT.
     */
    private String signature;

    public JWebToken(Payload payload) {
        this.payload = payload;
    }

    /**
     * header + payload
     *
     * @return header + payload
     */
    public String headerPayload() {
        if (ObjectHelper.isEmptyText(payloadJson))
            throw new IllegalArgumentException("Missing the JWT payload.");

        String p = new Base64Utils(payloadJson).setWithoutPadding(true).encodeAsString();

        return HEADER + "." + p;
    }

    /**
     * Generate the signature.
     *
     * @param secretKey The secret key
     * @return The signature
     */
    public String signature(String secretKey) {
        String headerPayload = headerPayload();

        return HashHelper.getHmacSHA256(headerPayload, secretKey, true);
    }

    /**
     * 返回 Token 的字符串形式
     *
     * @return Token
     */
    @Override
    public String toString() {
        return headerPayload() + "." + signature;
    }
}