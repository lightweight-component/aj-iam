package com.ajaxjs.iam.server.auth;

import com.ajaxjs.iam.server.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestEmailCodeService extends BaseTest {
    @Autowired
    EmailCodeService emailCodeService;

    @Test
    void testSendEmail() {
        emailCodeService.sendVerificationCode("sp42@qq.com");
    }
}
