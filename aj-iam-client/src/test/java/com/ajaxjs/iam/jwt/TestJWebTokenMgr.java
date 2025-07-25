package com.ajaxjs.iam.jwt;

import com.ajaxjs.util.JsonUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestJWebTokenMgr {
    private final JWebTokenMgr jWebTokenMgr = new JWebTokenMgr();

    @Test
    public void testParseValidToken() {
        String tokenStr = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
                "eyJzdWIiOiJ1c2VyIiwibmFtZSI6IkpvaG4gRG9lIiwiYXVkIjoiaW1hZ2UiLCJleHAiOjE2MTQ0NzIwMzQs" +
                "ImlzcyI6ImZvb0BiYXIuY29tIiwiaWF0IjoxNjE0NDY4MDM0fQ." +
                "C1Qc0RwN1V1S1V5T0VSWnBtR0VSWkFRT1lYUzRqT1V6T0VSVk1qWmhiR1V5T0VSVk1qWmhiR1U9";
        JWebToken token = jWebTokenMgr.parse(tokenStr);

        assertNotNull(token);
        assertEquals("user", token.getPayload().getSub());
        assertEquals("John Doe", token.getPayload().getName());
        assertEquals("image", token.getPayload().getAud());
        assertEquals("foo@bar.com", token.getPayload().getIss());
//        assertEquals(1614468034L, token.getPayload().getExp().longValue());
//        assertEquals(1614464034L, token.getPayload().getIat().longValue());
    }

    @Test
    public void testTokenFactory() {
        Payload payload = new Payload();
        payload.setSub("1234567890");
        payload.setName("John Doe");
        payload.setAud("https://api.foo.bar");
        long expires = JwtUtils.now() + 3600; // Expires in 1 hour
        payload.setExp(expires);

        JWebToken token = jWebTokenMgr.tokenFactory(payload);

        // Validate the token's structure and contents
        assertNotNull(token);
        assertNotNull(token.getPayload());
        assertNotNull(token.getPayloadJson());
        assertNotNull(token.getSignature());
        assertEquals(jWebTokenMgr.getIssuer(), token.getPayload().getIss());

        assertEquals(payload.getSub(), token.getPayload().getSub());
        assertEquals(payload.getName(), token.getPayload().getName());
        assertEquals(payload.getAud(), token.getPayload().getAud());

        // JSON payload comparison
        String expectedJson = JsonUtil.toJson(payload);
        assertEquals(expectedJson, token.getPayloadJson());
    }

    @Test
    public void testIsValid() {
        Payload payload = new Payload();
        payload.setSub("1234567890");
        payload.setName("John Doe");
        payload.setAud("https://api.foo.bar");
        long expires = JwtUtils.now() + 3600; // Expires in 1 hour
        payload.setExp(expires);

        JWebToken token = jWebTokenMgr.tokenFactory(payload);
        assertTrue(jWebTokenMgr.isValid(token));
    }

    @Test
    public void testTokenFactoryWithValidInput() {
        String sub = "user123";
        String name = "John Doe";
        String aud = "admin";
        long expires = System.currentTimeMillis() / 1000 + 3600; // Expires in 1 hour

        JWebToken token = jWebTokenMgr.tokenFactory(sub, name, aud, expires);

        assertNotNull(token, "Token should not be null");
        assertNotNull(token.getPayload(), "Payload should not be null");
        assertEquals("Subject mismatch", sub, token.getPayload().getSub());
        assertEquals("Name mismatch", name, token.getPayload().getName());
        assertEquals("Audience mismatch", aud, token.getPayload().getAud());
    }

    final static String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIiLCJleHAiOjAsImlhdCI6MTcwMDk0OTEzMSwiaXNzIjoiZm9vQGJhci5uZXQiLCJuYW1lIjoi6JeP57uP6ZiB572R56uZIiwic3ViIjoiNCJ9.m0A-ykfjcZsUYIIYsHqE8vEySisKztN2IhQMvbUfqZI";

    @Test
    public void test() {
        JWebTokenMgr mgr = new JWebTokenMgr();
        mgr.setSecretKey("aEsc65643vb3");
        JWebToken jwt = mgr.parse(token);

        boolean valid = mgr.isValid(jwt);
        System.out.println(valid);

        String jsonUser = "{\"id\": %s, \"name\": \"%s\"}";
        jsonUser = String.format(jsonUser, jwt.getPayload().getSub(), jwt.getPayload().getName());

        System.out.println(jsonUser);
    }
}
