package com.ajaxjs.iam.jwt;

import com.ajaxjs.util.date.Formatter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class JwtUtils {
    /**
     * 返回 EpochSecond 时间
     *
     * @return 当前时间
     */
    public static long now() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 返回过期时间
     *
     * @param hours 小时数
     * @return 过期时间的时间戳
     */
    public static long setExpire(int hours) {
        return now() + hours * 3600L;
    }

    /**
     * EpochSecond 时间戳转换为真实的时间
     *
     * @return 真实的时间
     */
    public static String toRealTime(long timestamp) {
        LocalDateTime localDateTime = Instant.ofEpochSecond(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();

        return Formatter.getDateTimeFormatter().format(localDateTime);
    }
}
