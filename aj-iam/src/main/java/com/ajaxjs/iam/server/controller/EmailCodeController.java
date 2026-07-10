package com.ajaxjs.iam.server.controller;

import com.ajaxjs.iam.annotation.AllowOpenAccess;
import com.ajaxjs.iam.jwt.JwtAccessToken;
import com.ajaxjs.spring.annotation.BizAction;
import org.springframework.web.bind.annotation.*;

/**
 * User login by email
 */
@RestController
@RequestMapping("/email_code")
public interface EmailCodeController {
    /**
     * 根据邮箱发送验证码
     *
     * @param email 邮箱
     * @param appId 应用 appId
     * @return 是否成功
     */
    @PostMapping("/send_verification_code")
    @BizAction("根据邮箱发送验证码")
    @AllowOpenAccess
    boolean sendVerificationCode(@RequestParam String email, @RequestParam String appId);

    /**
     * 检查验证码是否匹配且未过期
     *
     * @param email 邮箱
     * @param appId 应用 appId
     * @param code  验证码
     * @return 是否成功
     */
    @PostMapping("/verify_code")
    @BizAction("检查验证码是否匹配且未过期")
    @AllowOpenAccess
    JwtAccessToken verifyCode(@RequestParam String email, @RequestParam String appId, @RequestParam String code);

    /**
     * 检查验证码是否匹配且未过期
     * 如果匹配，则更新用户手机号码
     *
     * @param email 邮箱
     * @param appId 应用 appId
     * @param code  验证码
     * @return 验证码是否通过
     */
    @PostMapping("/verify_code_update_email")
    @BizAction("检查验证码是否匹配且未过期")
    boolean updateUserEmail(@RequestParam String email, @RequestParam String appId, @RequestParam String code);

    /**
     * 发送邮件
     *
     * @return
     */
    @GetMapping("/test")
    @AllowOpenAccess
    boolean test();
}
