package com.ajaxjs.iam.jwt;

import org.junit.jupiter.api.Test;

public class TestJWT {
    JWebTokenMgr mgr = new JWebTokenMgr();

    @Test
    public void testMakeToken() {
        JWebToken token = mgr.tokenFactory("1000", "user01", "admin, guest", JwtUtils.setExpire(24));
        System.out.println(token.toString());

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
        JWebToken jwt = mgr.parse("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJERUZBVUxUX1NDT1BFIiwiZXhwIjoxNzA5MjkwOTM2LCJpYXQiOjE3MDkyMDQ1MzYsImlzcyI6ImZvb0BiYXIubmV0IiwibmFtZSI6ImFkbWluIiwic3ViIjoiMSJ9.HZF89-4j2B5y22AbuB47ID0GCFuMxxbVur5zAdHFrOk");
        System.out.println(mgr.isValid("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJERUZBVUxUX1NDT1BFIiwiZXhwIjoxNzA5MjkwOTM2LCJpYXQiOjE3MDkyMDQ1MzYsImlzcyI6ImZvb0BiYXIubmV0IiwibmFtZSI6ImFkbWluIiwic3ViIjoiMSJ9.HZF89-4j2B5y22AbuB47ID0GCFuMxxbVur5zAdHFrOk"));
//        assertTrue(mgr.isValid("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhZG1pbiwgZ3Vlc3QiLCJleHAiOjE2ODc3MjE1NTIsImlhdCI6MTY4NzYzNTE1MiwiaXNzIjoiZm9vQGJhci5uZXQiLCJzdWIiOiJ1c2VyMDEifQ.IYommcWgSWAmQnVkkd9-aJ6smeuJ4cFoTBUzUXCltgE"));
    }

    @Test
    public void testToRealTime() {
        String realTime = JwtUtils.toRealTime(1721233114L);
        System.out.println(realTime);
    }
}
