package com.ajaxjs.iam.jwt;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class TestJWT {
    JWebTokenMgr mgr = new JWebTokenMgr();

    @Test
    public void testMakeToken() {
        JWebToken token = mgr.tokenFactory("1000", "user01", "admin, guest", JwtUtils.setExpire(24), null, null, null);
        System.out.println(token.toString().length());

        System.out.println(mgr.isValid(token.toString()));
    }

    @Test
    public void testValid2() {
        System.out.println(mgr.isValid("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIzMyIsIm5hbWUiOiJKYWNrODg4OCIsImF1ZCI6bnVsbCwiZXhwIjoxNzUwODkxODg1LCJpc3MiOiJmb29AYmFyLm5ldCIsImlhdCI6MTc1MDg3Mzg4NX0.SrfQsJQkfa1aDSk2slA6BAJsGckzGNOrQlVU2OVDfcM"));
    }

    @Test
    public void testValid() {
        JWebTokenMgr mgr = new JWebTokenMgr();
        mgr.setSecretKey("aEsD65643vb3");
        JWebToken jwt = JWebTokenMgr.parse("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJERUZBVUxUX1NDT1BFIiwiZXhwIjoxNzA5MjkwOTM2LCJpYXQiOjE3MDkyMDQ1MzYsImlzcyI6ImZvb0BiYXIubmV0IiwibmFtZSI6ImFkbWluIiwic3ViIjoiMSJ9.HZF89-4j2B5y22AbuB47ID0GCFuMxxbVur5zAdHFrOk");
        System.out.println(mgr.isValid("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJERUZBVUxUX1NDT1BFIiwiZXhwIjoxNzA5MjkwOTM2LCJpYXQiOjE3MDkyMDQ1MzYsImlzcyI6ImZvb0BiYXIubmV0IiwibmFtZSI6ImFkbWluIiwic3ViIjoiMSJ9.HZF89-4j2B5y22AbuB47ID0GCFuMxxbVur5zAdHFrOk"));
//        assertTrue(mgr.isValid("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhZG1pbiwgZ3Vlc3QiLCJleHAiOjE2ODc3MjE1NTIsImlhdCI6MTY4NzYzNTE1MiwiaXNzIjoiZm9vQGJhci5uZXQiLCJzdWIiOiJ1c2VyMDEifQ.IYommcWgSWAmQnVkkd9-aJ6smeuJ4cFoTBUzUXCltgE"));
    }

    @Test
    public void testNow() {
        long timestamp = System.currentTimeMillis() / 1000;
        System.out.println(timestamp);
        System.out.println(JwtUtils.toRealTime(timestamp));

        timestamp= LocalDateTime.now().atZone(ZoneId.systemDefault()) // <-- 关键：关联时区
                .toInstant().getEpochSecond();

        System.out.println(timestamp);
        System.out.println(JwtUtils.toRealTime(timestamp));

    }

    @Test
    public void testToRealTime() {
        String realTime = JwtUtils.toRealTime(1721233114L);
        System.out.println(realTime);

        realTime = JwtUtils.toRealTime(JwtUtils.now());
        System.out.println(realTime);
    }
}
