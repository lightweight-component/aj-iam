package com.ajaxjs.iam.jwt;

import org.junit.Test;

public class TestJwtUtils {
    @Test
    public void testToRealTime(){
        String realTime = JwtUtils.toRealTime(1721233114L);
        System.out.println(realTime);
    }
}
