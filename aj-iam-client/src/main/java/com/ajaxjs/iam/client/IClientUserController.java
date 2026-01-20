package com.ajaxjs.iam.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;

public interface IClientUserController {
    @GetMapping("/to_login")
    RedirectView loginPageUrl(@RequestParam(required = false) String callback_url, @RequestParam(required = false) String web_url);

    @GetMapping("/callback")
    ModelAndView callbackToken(@RequestParam String code, @RequestParam String state, @RequestParam(required = false) String web_url, HttpServletResponse resp);

    /**
     * 奇怪 POST 不行，GET 又可以
     *
     * @param resp
     * @return
     */
    @RequestMapping("/logout")
    boolean logout(HttpServletResponse resp);
}
