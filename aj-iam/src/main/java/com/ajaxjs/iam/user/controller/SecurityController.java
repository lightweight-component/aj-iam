package com.ajaxjs.iam.user.controller;

import com.ajaxjs.framework.database.IgnoreDataBaseConnect;
import com.ajaxjs.security.captcha.image.ImageCaptcha;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/security")
@RequiredArgsConstructor
public class SecurityController {
    private final ImageCaptcha imageCaptcha;

    @GetMapping("/captcha")
    @IgnoreDataBaseConnect
    void showCaptcha(HttpServletRequest req, HttpServletResponse response) {
        imageCaptcha.captchaImage(req, response);
    }
}
