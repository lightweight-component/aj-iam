package com.ajaxjs.iam.client;

import com.ajaxjs.iam.client.filter.UserInterceptor;
import com.ajaxjs.iam.jwt.JWebTokenMgr;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.ajaxjs.iam.UserConstants.ACCESS_TOKEN_KEY;
import static com.ajaxjs.iam.UserConstants.REFRESH_TOKEN_KEY;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestRefreshToken {

    @Test
    void testInterceptor() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        UserInterceptor interceptor = new UserInterceptor();
        interceptor.setRun(true);
        interceptor.setCacheType("jwt");

        JWebTokenMgr mgr = new JWebTokenMgr();
//        mgr.setSecretKey("aEsD65643vb2");
        interceptor.setJWebTokenMgr(mgr);
        interceptor.setIamService("http://local.zhongen.com");
        interceptor.setClientId("lKi9p9FyicBd9eb");
        interceptor.setClientSecret("zLkv9ngl8mnF5KkKtKEbtCeLC4");

        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwiYXVkIjoidXNlciIsImV4cCI6MTc1NjA5OTY5MCwiaXNzIjoiZm9vQGJhci5uZXQiLCJpYXQiOjE3NTYxMzU2OTB9.3NdDBP4NK0Isaf56xY8CGFvBgl0iHbKKzBsGHoC/zxM";
        when(request.getParameter(ACCESS_TOKEN_KEY)).thenReturn(token);
        when(request.getParameter(REFRESH_TOKEN_KEY)).thenReturn("d387ea24-9d4f-4c12-bf29-8102402e8781");

        interceptor.preHandle(request, resp, null);
    }
}
