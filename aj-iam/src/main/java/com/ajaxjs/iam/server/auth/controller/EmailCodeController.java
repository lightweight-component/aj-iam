package com.ajaxjs.iam.server.auth.controller;

import com.ajaxjs.iam.annotation.ClientAuthentication;
import com.ajaxjs.iam.jwt.JwtToken;
import com.ajaxjs.spring.annotation.BizAction;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通过邮箱验证码登录
 */
@RestController
@RequestMapping("/email_code")
public interface EmailCodeController {
    /**
     * 根据邮箱发送验证码
     *
     * @param email 邮箱
     * @return 是否成功
     */
    @PostMapping("/send_verification_code")
    @BizAction("根据邮箱发送验证码")
    @ClientAuthentication
    boolean sendVerificationCode(@RequestParam String email);

    /**
     * 检查验证码是否匹配且未过期
     *
     * @param email 邮箱
     * @param code  验证码
     * @return 是否成功
     */
    @PostMapping("/verify_code")
    @BizAction("检查验证码是否匹配且未过期")
    @ClientAuthentication
    JwtToken verifyCode(@RequestParam String email, @RequestParam String code);

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
}
