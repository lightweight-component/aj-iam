package com.ajaxjs.iam.server.user_info.controller;

import com.ajaxjs.iam.annotation.AllowOpenAccess;
import com.ajaxjs.iam.annotation.ClientAuthentication;
import com.ajaxjs.security.captcha.image.ImageCaptchaCheck;
import com.ajaxjs.spring.annotation.BizAction;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户注册
 */
@RestController
@RequestMapping("/user/register")
public interface UserRegisterController {
    /**
     * 用户注册
     *
     * @param params 用户参数
     * @return 是否成功
     */
    @PostMapping("/web")
    @AllowOpenAccess
    @ImageCaptchaCheck
    @BizAction("用户注册")
    boolean registerWeb(@RequestBody Map<String, Object> params);

    /**
     * 用户注册（通过客户端认证）
     *
     * @param params 用户参数
     * @return 是否成功
     */
    @PostMapping
    @ClientAuthentication
    @BizAction("用户注册")
    boolean register(@RequestBody Map<String, Object> params);

    /**
     * 检查用户某个值是否已经存在一样的值
     * 用于前端校验调用
     *
     * @param field 字段名，当前只能是 username/email/phone 中的任意一种
     * @param value 字段值，要校验的值
     * @return true=存在
     */
    @GetMapping("/checkRepeat")
    @BizAction("查用户某个值是否已经存在一样的值")
    @AllowOpenAccess
    boolean checkRepeat(@RequestParam String field, @RequestParam String value);
}
