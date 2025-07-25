package com.ajaxjs.iam.server.service;

import com.ajaxjs.iam.jwt.JWebTokenMgr;
import com.ajaxjs.iam.server.BaseTest;
import com.ajaxjs.iam.server.common.IamConstants;
import com.ajaxjs.iam.server.model.JwtAccessToken;
import com.ajaxjs.iam.user.model.User;
import com.ajaxjs.util.EncodeTools;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestOidcService extends BaseTest {
    @Autowired
    OidcService OidcService;

    @Test
    public void testCreateAuthorizationCode() {
        User user = new User();
        user.setId(1L);
        user.setLoginId("admin");

//        ModelAndView mv = OidcService.authorization("C2Oj5hKcwMmgxiKygwquLCSN", "http://qq.com", null, null);
//        System.out.println(mv);
    }

    public static String encodeClient(String clientId, String clientSecret) {
        String clientAndSecret = clientId + ":" + clientSecret;

        return "Basic " + EncodeTools.base64EncodeToString(clientAndSecret);
    }

    @Test
    public void testClient() {
        String s = encodeClient("G5IFeG7Eesbny3f", "J1Bb4zhchfziuDipKI7sgo6iyk");

        JwtAccessToken jwtAccessTokenResult = OidcService.clientCredentials(IamConstants.GrantType.CLIENT_CREDENTIALS, s);
        System.out.println(jwtAccessTokenResult.getAccess_token());
        System.out.println(jwtAccessTokenResult.getId_token());
    }

    @Test
    public void createToken() {
        User user = new User();
        user.setId(1L);
        user.setName("admin");
        user.setLoginId("admin");

        OidcService.getCache().put("1:user", user, 0);
        OidcService.getCache().put("1:scope", "user", 0);

        String s = encodeClient("G5IFeG7Eesbny3f", "J1Bb4zhchfziuDipKI7sgo6iyk");
        JwtAccessToken token = OidcService.token(s, "authorization_code", "1", "57458", "kjkkk");
        System.out.println(token.getId_token());

        JWebTokenMgr mgr = new JWebTokenMgr();
        mgr.setSecretKey("aEsD65643vb2");
        System.out.println(mgr.isValid(token.getId_token()));
    }

    @Test
    public void testRopcToken() {
        OidcService.ropcToken("password", "Jack8888", "jTomcom@1120", "r3fgD3x43ft5H", "zKvmMA4KmJ2CIijl9ubqbXpHm1", null);
    }
}
