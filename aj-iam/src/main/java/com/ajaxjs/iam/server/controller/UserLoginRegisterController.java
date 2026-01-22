package com.ajaxjs.iam.server.controller;

//import com.ajaxjs.framework.filter.google_captcha.GoogleCaptchaCheck;

import com.ajaxjs.framework.mvc.unifiedreturn.BizAction;
import com.ajaxjs.iam.annotation.AllowOpenAccess;
import com.ajaxjs.iam.jwt.JwtAccessToken;
import com.ajaxjs.security.captcha.image.ImageCaptchaCheck;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户注册
 */
@RestController
@RequestMapping("/user")
public interface UserLoginRegisterController {
    /**
     * 用户登录
     *
     * @param username 用户名/手机号/邮箱
     * @param password 密码
     * @param appId    应用 id
     * @return 应用的 JWT AccessToken
     */
    @PostMapping("/login")
    @AllowOpenAccess
//    @ImageCaptchaCheck
    @BizAction("用户登录")
    JwtAccessToken login(@RequestParam String username, @RequestParam String password, @RequestParam String appId);

    /**
     * 用户注册
     * <p>
     *
     * @param params 用户参数
     * @return 是否成功
     */
    @PostMapping
    @AllowOpenAccess
    @ImageCaptchaCheck
    @BizAction("用户注册")
    Boolean register(@RequestBody Map<String, Object> params);

    /**
     * 检查用户某个值是否已经存在一样的值
     *
     * @param field 字段名，当前只能是 username/email/phone 中的任意一种
     * @param value 字段值，要校验的值
     * @return true=存在
     */
    @GetMapping("/checkRepeat")
    @BizAction("查用户某个值是否已经存在一样的值")
    Boolean checkRepeat(@RequestParam String field, @RequestParam String value);
}
