package com.ajaxjs.iam.server.service;

import com.ajaxjs.iam.BaseTest;
import com.ajaxjs.iam.server.common.IamConstants;
import com.ajaxjs.iam.model.AccessToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

public class TestOAuthService extends BaseTest {
    @Autowired
    OAuthService oAuthService;

    @Autowired
    WebApplicationContext wac;

//    @Test
//    public void testGetAuthCode() {
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("admin");
//        MockHttpSession sessionPub = new MockHttpSession();
//        sessionPub.setAttribute(UserConstant.USER_SESSION_KEY, user);
//
//        // @formatter:off
//        MockHttpServletRequestBuilder req = get("/sso/authorize_code").
//                param("redirect_uri", "https://www.qq.com").param("client_id", "dss23s").
//                session(sessionPub);
//        // @formatter:on
//
//        ModelAndView m = oAuthService.getAuthorizeCode("dss23s", "https://www.qq.com", "", "", req.buildRequest(Objects.requireNonNull(wac.getServletContext())));
//        TestHelper.printJson(m);
//    }

    @Test
    public void testClientCredentials() {
        AccessToken accessToken = oAuthService.clientCredentials("clientCredentials", "G5IFeG7Eesbny3f", "J1Bb4zhchfziuDipKI7sgo6iyk");
        System.out.println(accessToken);
    }

    @Test
    public void testRefreshToken() {
        AccessToken accessToken = oAuthService.refreshToken(IamConstants.GrantType.REFRESH_TOKEN, "G5IFeG7Eesbny3f", "bf173e7d-1131-89d3-e1f7-96a236571681");
        System.out.println(accessToken);
    }
}
