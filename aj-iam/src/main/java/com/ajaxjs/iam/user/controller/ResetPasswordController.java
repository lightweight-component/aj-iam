package com.ajaxjs.iam.user.controller;

import com.ajaxjs.iam.annotation.AllowOpenAccess;
import com.ajaxjs.iam.user.service.resetpsw.ResetPasswordByEmailCode;
import com.ajaxjs.iam.user.service.resetpsw.ResetPasswordByEmailLink;
import com.ajaxjs.iam.user.service.resetpsw.ResetPasswordBySmsCode;
import com.ajaxjs.security.captcha.image.ImageCaptchaCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 重置密码
 *
 * @author Frank Cheung
 */
@RestController
@RequestMapping("/reset_psw")
@AllowOpenAccess
public class ResetPasswordController {
    @Autowired
    ResetPasswordByEmailCode resetPasswordByEmailCode;

    /**
     * 根据 email 重置密码 BY_CODE
     *
     * @param email 用户邮件
     * @return true 表示发送成功
     */
    @PostMapping("/send_reset_email_code/{email}")
    @ImageCaptchaCheck
    public boolean sendCodeEmail(@PathVariable String email) {
        return resetPasswordByEmailCode.sendCode(email);
    }

    /**
     * 校验 Email code 并更新密码。
     * <p>
     * 根据邮件查询用户，验证 token，若通过更新密码
     *
     * @param code   用户令牌
     * @param newPsw 用户输入的新密码
     * @param email  用户邮件
     */
    @PostMapping("/verify_email_code_update_psw/{email}")
    public boolean verifyEmailCodesUpdatePsw(@RequestParam String code, @RequestParam String newPsw, @PathVariable String email) {
        return resetPasswordByEmailCode.verifyCodeUpdatePsw(code, newPsw, email);
    }

    @Autowired
    ResetPasswordBySmsCode resetPasswordBySmsCode;

    /**
     * 根据 手机 重置密码
     *
     * @param phone 手机号码
     * @return true 表示发送成功
     */
    @PostMapping("/send_reset_phone/{phone}")
    public boolean sendRestPhone(@PathVariable String phone) {
        return resetPasswordBySmsCode.sendCode(phone);
    }

    /**
     * 校验 Sms code 并更新密码。
     * <p>
     * 根据 手机  查询用户，验证 token，若通过更新密码
     *
     * @param code   用户令牌
     * @param newPsw 用户输入的新密码
     * @param phone  手机
     */
    @PostMapping("/verify_sms_update_psw/{phone}/{code}")
    public boolean verifySmsUpdatePsw(@PathVariable String code, @RequestParam String newPsw, @PathVariable String phone) {
        return resetPasswordBySmsCode.verifyCodeUpdatePsw(code, newPsw, phone);
    }

    @Autowired
    ResetPasswordByEmailLink resetPasswordByEmailLink;

    /**
     * 根据 email 重置密码 BY_LINK
     *
     * @param email 用户邮件
     * @return true 表示发送成功
     */
    @PostMapping("/send_reset_email_link/{email}")
    public boolean sendRestEmail(@PathVariable String email) {
        return resetPasswordByEmailLink.sendRestEmail(email);
    }

    /**
     * 校验 Token 并更新密码。
     * <p>
     * 前后端分离模式下，前端是纯静态页面，得到 email 和 code，先展现页面允许修改密码(即使不合法code 或超时的也没关系)。
     * 到时提交到这个接口，验证 token，若通过更新密码。这个不足是进入页面没有马上提示 code 是否合法，而是执行提交才知道，不过也没太大关系。
     * 根据邮件查询用户，验证 token，若通过更新密码
     *
     * @param token  用户令牌
     * @param newPsw 用户输入的新密码
     * @param email  邮件地址
     * @return true 表示更新成功
     */
    @PostMapping("/verify_email_update_psw/{email}")
    public boolean verifyEmailUpdatePsw(@RequestParam String token, @RequestParam String newPsw, @PathVariable String email) {
        return resetPasswordByEmailLink.verifyEmailUpdatePsw(token, newPsw, email);
    }
}
