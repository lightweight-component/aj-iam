package com.ajaxjs.iam.server.auth.controller;

import com.ajaxjs.iam.annotation.AllowOpenAccess;
import com.ajaxjs.iam.annotation.ClientAuthentication;
import com.ajaxjs.iam.jwt.JwtToken;
import com.ajaxjs.security.captcha.image.ImageCaptchaCheck;
import com.ajaxjs.spring.annotation.BizAction;
import org.springframework.web.bind.annotation.*;

/**
 * 短信发送
 */
@RestController
@RequestMapping("/sms")
public interface SmsController {
    /**
     * APP 登录一键登录，使用阿里云的
     * 自动获取手机号码。单屏手机号码登录
     *
     * @param token 登录 token
     * @return 登录结果
     */
    @GetMapping("/mobile_app_onekey_login_ali/{token}")
    @BizAction("APP 登录一键登录")
    @ClientAuthentication
    JwtToken mobileAppOneKeyLoginAli(@PathVariable String token);

    /**
     * 根据手机号码发送短信
     *
     * @param phone 手机号码
     * @return 是否成功
     */
    @PostMapping("/send_verification_code")
    @BizAction("根据手机号码发送短信")
    @ClientAuthentication
    boolean sendVerificationCode(@RequestParam String phone);

    /**
     * 根据手机号码发送短信
     *
     * @param phone       手机号码
     * @param smsClientId 登记发送短信的客户端
     * @return 是否成功
     */
    @PostMapping("/send_verification_code_web")
    @BizAction("根据手机号码发送短信")
    @ImageCaptchaCheck
    @AllowOpenAccess
    boolean sendVerificationCodeWeb(@RequestParam String phone, @RequestParam String smsClientId);

    /**
     * 用户登录
     * <p>
     * 如果登录成功，前端应该跳转到 OIDC 获取 code 的流程
     *
     * @param phone 手机号码
     * @param code  验证码
     * @return 是否成功
     */
    @PostMapping("/sms_login")
    @BizAction("用户登录 loginBySms")
    @ImageCaptchaCheck
    @AllowOpenAccess
    boolean loginBySms(@RequestParam String phone, @RequestParam String code);

    /**
     * 检查验证码是否匹配且未过期
     *
     * @param phone 手机号码
     * @param code  验证码
     * @return 是否成功
     */
    @PostMapping("/verify_code")
    @BizAction("检查验证码是否匹配且未过期")
    @ClientAuthentication
    JwtToken verifyCode(@RequestParam String phone, @RequestParam String code);

    /**
     * 检查验证码是否匹配且未过期
     * 如果匹配，则更新用户手机号码
     *
     * @param phone 手机号码
     * @param appId 应用 appId
     * @param code  验证码
     * @return 验证码是否通过
     */
    @PostMapping("/verify_code_update_phone")
    @BizAction("检查验证码是否匹配且未过期")
    boolean updateUserPhone(@RequestParam String phone, @RequestParam String appId, @RequestParam String code);

    @GetMapping("/test")
    @AllowOpenAccess
    boolean test();
}