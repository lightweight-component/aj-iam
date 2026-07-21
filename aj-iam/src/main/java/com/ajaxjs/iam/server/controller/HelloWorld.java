package com.ajaxjs.iam.server.controller;

import com.ajaxjs.framework.database.IgnoreDataBaseConnect;
import com.ajaxjs.iam.annotation.AllowOpenAccess;
import com.ajaxjs.framework.mvc.unifiedreturn.ResponseResultWrapper;
import com.ajaxjs.security.captcha.image.ImageCaptcha;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class HelloWorld {
    @GetMapping
    @IgnoreDataBaseConnect
    @AllowOpenAccess
    @CrossOrigin
    ResponseResultWrapper sayHi() {
        ResponseResultWrapper result = new ResponseResultWrapper();
        result.setStatus(1);
        result.setData("Hello World");

        return result;
    }

    private final ImageCaptcha imageCaptcha;

    @GetMapping("/captcha")
    @IgnoreDataBaseConnect
    @AllowOpenAccess
    void showCaptcha(HttpServletRequest req, HttpServletResponse response) {
        imageCaptcha.captchaImage(req, response);
    }
}
