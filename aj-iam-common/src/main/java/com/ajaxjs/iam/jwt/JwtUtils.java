package com.ajaxjs.iam.jwt;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

public class JwtUtils {
    /**
     * Sign with HMAC SHA256 (HS256)
     */
    public static String hmacSha256(String data, String secret) {
        try {
            byte[] hash = secret.getBytes(StandardCharsets.UTF_8);
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            sha256Hmac.init(new SecretKeySpec(hash, "HmacSHA256"));
            byte[] signedBytes = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            return encode(signedBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encode(String str) {
        return encode(str.getBytes(StandardCharsets.UTF_8));
    }

    public static String encode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /**
     * 返回 EpochSecond 时间
     *
     * @return 当前时间
     */
    public static long now() {
        return LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }

    /**
     * 返回过期时间
     *
     * @param hours 小时数
     * @return 过期时间的时间戳
     */
    public static long setExpire(int hours) {
        return LocalDateTime.now().plus(hours, ChronoUnit.HOURS).toEpochSecond(ZoneOffset.UTC);
    }

    /**
     * EpochSecond 时间戳转换为真实的时间
     *
     * @return 真实的时间
     */
    public static String toRealTime(long timestamp) {
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        LocalDateTime localDateTime = java.time.Instant.ofEpochSecond(timestamp).atZone(zoneId).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return formatter.format(localDateTime);
    }
}
