package com.ajaxjs.iam.server.service;

import com.ajaxjs.iam.server.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestEmailCodeService extends BaseTest {
    @Autowired
    private EmailCodeService emailCodeService;

    @Test
    void testSendEmailCode() {
        emailCodeService.sendEmailCode();
    }
}
