package com.ajaxjs.iam.user.service;

import com.ajaxjs.iam.server.BaseTest;
import com.ajaxjs.iam.UserConstants;
import com.ajaxjs.iam.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestUserService extends BaseTest {
    @Autowired
    UserService userService;

    @Test
    public void testInfo() {
        assertNotNull(userService);
        User user = userService.info(1L);
        System.out.println(user);

        user = userService.info(1L);
        System.out.println(user);
    }

    @Test
    public void testCreate() {
        User user = new User();
        user.setLoginId("TesdAdmin");
        user.setGender(UserConstants.Gender.MALE);
        user.setBirthday(new Date());
        user.setAvatar("https://example.com/avatar.jpg");
        user.setEmail("johndo@eexample.com");
        user.setPhone("1234567890");
        user.setIdCardNo("123456789012345678");
        assertNotNull(userService);

//        Long userNewlyId = userService.create(user);
//        assertNotNull(userNewlyId);
//        System.out.println(userNewlyId);
    }

    @Test
    public void testUpdate() {
        User user = new User();
        user.setId(316L);
        user.setLoginId("John Doe");
        user.setGender(UserConstants.Gender.MALE);
        user.setBirthday(new Date());
        user.setAvatar("https://example.com/avatar.jpg");
        user.setEmail("johndo@eexample.com");
        user.setPhone("1234567890");
        user.setIdCardNo("123456789012345678");
        assertNotNull(userService);

        assertTrue(userService.update(user));
    }
}
