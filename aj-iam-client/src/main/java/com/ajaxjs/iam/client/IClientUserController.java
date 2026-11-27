package com.ajaxjs.iam.client;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public interface IClientUserController {
    @GetMapping("/to_login")
    RedirectView loginPageUrl(@RequestParam(required = false) String callback_url, @RequestParam(required = false) String web_url);

    @GetMapping("/callback")
    ModelAndView callbackToken(@RequestParam String code, @RequestParam String state, @RequestParam(required = false) String web_url, HttpServletResponse resp);

    /**
     * 奇怪 POST 不行，GET 又可以
     *
     * @param resp 响应对象
     * @return 是否成功
     */
    @RequestMapping("/logout")
    boolean logout(HttpServletResponse resp);
}