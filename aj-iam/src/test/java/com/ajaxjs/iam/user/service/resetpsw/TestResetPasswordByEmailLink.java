package com.ajaxjs.iam.user.service.resetpsw;

import com.ajaxjs.framework.spring.SimpleTemplate;
import com.ajaxjs.iam.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import static com.ajaxjs.iam.user.service.resetpsw.ResetPasswordByEmailLink.BY_LINK_HTML;
import static org.junit.jupiter.api.Assertions.*;

public class TestResetPasswordByEmailLink extends BaseTest {
    @Test
    void testTpl() {
        // 设置文字模板，其中 #{} 表示表达式的起止，#user 是表达式字符串，表示引用一个变量。
        String template = "Hi，${user}，早上好";

        Map<String, String> map = new HashMap<>();
        map.put("user", "黎明");

        String result = SimpleTemplate.render(template, map);
        assertEquals("Hi，黎明，早上好", result);

        map = new HashMap<>(16);
        map.put("username", "黎明");
        map.put("timeout", "12");

        result = SimpleTemplate.render(BY_LINK_HTML, map);
        System.out.println(result);
    }

    @Autowired
    ResetPasswordByEmailLink resetPasswordByEmailLink;

    @Test
    public void makeEmailToken() {
        String token = resetPasswordByEmailLink.makeEmailToken("sp42@qq.com", 1);
        System.out.println(token);
        assertNotNull(token);

        assertTrue(resetPasswordByEmailLink.checkEmailToken(token, "sp42@qq.com"));
    }

    @Test
    public void verifyTokenUpdatePsw() {
        String token = "e896fcce344398037695f3b1b3703f0c1823e09aFA0197C41BEE5E80F0075A0F4C4C8730";
        resetPasswordByEmailLink.verifyEmailUpdatePsw(token, "123123", "sp42@qq.com");
    }
}
