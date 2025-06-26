package com.ajaxjs.iam.jwt;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class JwtUtils {
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
