package com.ajaxjs.iam.jwt;

import com.ajaxjs.iam.resource_server.Utils;
import org.junit.Test;
import org.junit.Assert;

public class JWebTokenMgrTest {
    private JWebTokenMgr jWebTokenMgr = new JWebTokenMgr();

    @Test
    public void testParseValidToken() {
        String tokenStr = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
                "eyJzdWIiOiJ1c2VyIiwibmFtZSI6IkpvaG4gRG9lIiwiYXVkIjoiaW1hZ2UiLCJleHAiOjE2MTQ0NzIwMzQs" +
                "ImlzcyI6ImZvb0BiYXIuY29tIiwiaWF0IjoxNjE0NDY4MDM0fQ." +
                "C1Qc0RwN1V1S1V5T0VSWnBtR0VSWkFRT1lYUzRqT1V6T0VSVk1qWmhiR1V5T0VSVk1qWmhiR1U9";
        JWebToken token = jWebTokenMgr.parse(tokenStr);

        Assert.assertNotNull(token);
        Assert.assertEquals("user", token.getPayload().getSub());
        Assert.assertEquals("John Doe", token.getPayload().getName());
        Assert.assertEquals("image", token.getPayload().getAud());
        Assert.assertEquals("foo@bar.com", token.getPayload().getIss());
//        Assert.assertEquals(1614468034L, token.getPayload().getExp().longValue());
//        Assert.assertEquals(1614464034L, token.getPayload().getIat().longValue());
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
        Assert.assertNotNull(token);
        Assert.assertNotNull(token.getPayload());
        Assert.assertNotNull(token.getPayloadJson());
        Assert.assertNotNull(token.getSignature());
        Assert.assertEquals(jWebTokenMgr.getIssuer(), token.getPayload().getIss());

        Assert.assertEquals(payload.getSub(), token.getPayload().getSub());
        Assert.assertEquals(payload.getName(), token.getPayload().getName());
        Assert.assertEquals(payload.getAud(), token.getPayload().getAud());

        // JSON payload comparison
        String expectedJson = Utils.bean2json(payload);
        Assert.assertEquals(expectedJson, token.getPayloadJson());
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
        Assert.assertTrue(jWebTokenMgr.isValid(token));
    }

    @Test
    public void testTokenFactoryWithValidInput() {
        String sub = "user123";
        String name = "John Doe";
        String aud = "admin";
        long expires = System.currentTimeMillis() / 1000 + 3600; // Expires in 1 hour

        JWebToken token = jWebTokenMgr.tokenFactory(sub, name, aud, expires);

        Assert.assertNotNull("Token should not be null", token);
        Assert.assertNotNull("Payload should not be null", token.getPayload());
        Assert.assertEquals("Subject mismatch", sub, token.getPayload().getSub());
        Assert.assertEquals("Name mismatch", name, token.getPayload().getName());
        Assert.assertEquals("Audience mismatch", aud, token.getPayload().getAud());
    }
}
