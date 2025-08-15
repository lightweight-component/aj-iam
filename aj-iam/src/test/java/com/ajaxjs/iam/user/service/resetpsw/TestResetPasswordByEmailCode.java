package com.ajaxjs.iam.user.service.resetpsw;

import com.ajaxjs.iam.BaseTest;
import com.ajaxjs.iam.user.model.User;
import com.ajaxjs.iam.user.service.ResetPasswordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.ajaxjs.iam.user.service.TenantService.AUTH_TENANT_ID;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestResetPasswordByEmailCode extends BaseTest {
    HttpServletRequest req = mock(HttpServletRequest.class);

    @Autowired
    ResetPasswordByEmailCode resetPasswordByEmailCode;

    @Test
    void testSendCodeEmail() {
        when(req.getHeader(AUTH_TENANT_ID)).thenReturn("1");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(req));

        boolean b = resetPasswordByEmailCode.sendCode("sp42@qq.com");
        assertTrue(b);
    }

    @Test
    void testVerifyEmailUpdatePsw() {
        when(req.getHeader(AUTH_TENANT_ID)).thenReturn("1");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(req));

        User user = ResetPasswordService.findUserBy("email", "sp42@qq.com", 1);
        String code = resetPasswordByEmailCode.saveCode(user, "sp42@qq.com", 1);

        boolean b = resetPasswordByEmailCode.verifyCodeUpdatePsw(code, "nba1234a", "sp42@qq.com");
        assertTrue(b);
    }
}
