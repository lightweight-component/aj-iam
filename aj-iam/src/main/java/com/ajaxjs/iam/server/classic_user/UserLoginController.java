package com.ajaxjs.iam.server.classic_user;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user/login")
public interface UserLoginController {
    /**
     * 检测用户是否已经登录
     *
     * @return true 表示已登录
     */
    @GetMapping("/is_login")
    Boolean isLogin();

    /**
     * 普通用户登录
     *
     * @param loginId   登录账号，用户标识，可以是 username/email/phone 中的一种，后台自动判断
     * @param password  密码
     * @param returnUrl 跳转地址
     * @return 若成功登录跳转
     */
    @PostMapping("/login")
    boolean login(@RequestParam String loginId, @RequestParam String password, @RequestParam(required = false) String returnUrl, HttpServletRequest req, HttpServletResponse resp);

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    boolean logout(@RequestParam(required = false) String returnUrl, HttpServletResponse resp, HttpSession session);
}
