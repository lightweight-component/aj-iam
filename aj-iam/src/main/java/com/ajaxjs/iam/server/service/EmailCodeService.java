package com.ajaxjs.iam.server.service;

import com.ajaxjs.framework.cache.Cache;
import com.ajaxjs.framework.cache.delayqueue.ExpiryCache;
import com.ajaxjs.iam.jwt.JwtAccessToken;
import com.ajaxjs.iam.server.common.UserUtils;
import com.ajaxjs.iam.server.common.langs.LanguageMapping;
import com.ajaxjs.iam.server.controller.EmailCodeController;
import com.ajaxjs.iam.server.model.AppSecretMgr;
import com.ajaxjs.message.email.Email;
import com.ajaxjs.message.email.resend.Resend;
import com.ajaxjs.sqlman.Action;
import com.ajaxjs.util.RandomTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class EmailCodeService implements EmailCodeController {
    private final Cache<String, Object> cache = ExpiryCache.getInstance();

    @Override
    public boolean sendVerificationCode(String email, String appId) {
        if (!StringUtils.hasText(email) || !UserUtils.isValidEmail(email)) // 请提交有效的邮箱
            throw new IllegalArgumentException(LanguageMapping.getLanguageByKey("sms.phone.invalid"));

        AppSecretMgr appSecretMgr = new Action("SELECT app_id, app_secret FROM app_secret_mgr WHERE owner = ?").query(appId).one(AppSecretMgr.class);

        if (appSecretMgr == null) // 请提供有效的 App 信息
            throw new NullPointerException(LanguageMapping.getLanguageByKey("sms.phone.provideAppInfo"));

        String randCode = RandomTools.generateRandomString(6);
        log.info("发送验证码：{} 到邮箱 {}", randCode, email);

        cache.put(email, randCode, 60 * 15);
        return false;
    }

    @Value("${SendEmail.resendKey}")
    private String resendKey;

    public boolean sendEmailCode() {
        Email emailEntity = new Email().setFrom("onboarding@resend.dev").setTo("sp42@qq.com").setSubject("测试邮件").setContent("测试邮件");
        return new Resend().setApiKey(resendKey).sendEmail(emailEntity);
    }

    @Override
    public JwtAccessToken verifyCode(String email, String appId, String code) {
        return null;
    }

    @Override
    public boolean updateUserEmail(String email, String appId, String code) {
        return false;
    }

    @Override
    public boolean test() {
        return sendEmailCode();
    }
}
