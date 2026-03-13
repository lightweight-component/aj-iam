package com.ajaxjs.iam.jwtv2;

import com.ajaxjs.util.Base64Utils;
import com.ajaxjs.util.HashHelper;
import com.ajaxjs.util.JsonUtil;
import lombok.Data;

@Data
public class Jwt {
    Header header;

    Payload payload;

    String signature;

    /**
     * header + payload
     */
    String headerPayload;

    /**
     * Transform to JWT parts to JSON string, and then encode to Base64.
     *
     * @return Jwt
     */
    public Jwt encode() {
        String headerJson = JsonUtil.toJson(header);
        String headerBase64 = new Base64Utils(headerJson).setWithoutPadding(true).encodeAsString();

        String payloadJson = JsonUtil.toJson(header);
        String payloadBase64 = new Base64Utils(payloadJson).setWithoutPadding(true).encodeAsString();

        headerPayload = headerBase64 + "." + payloadBase64;

        return this;
    }

    /**
     * Generate the signature.
     *
     * @param secretKey The secret key
     * @return Jwt
     */
    public Jwt makeSignature(String secretKey) {
        signature = HashHelper.getHmacSHA256(headerPayload, secretKey, true);

        return this;
    }

    public String get() {
        return headerPayload + "." + signature;
    }
}
