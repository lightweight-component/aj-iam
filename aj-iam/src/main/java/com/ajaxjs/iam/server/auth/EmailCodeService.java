package com.ajaxjs.iam.server.auth;

import com.ajaxjs.framework.cache.Cache;
import com.ajaxjs.framework.cache.delayqueue.ExpiryCache;
import com.ajaxjs.iam.jwt.JWebTokenMgr;
import com.ajaxjs.iam.jwt.JwtAccessToken;
import com.ajaxjs.iam.server.auth.apponekeylogin.LoginOrRegister;
import com.ajaxjs.iam.server.auth.controller.EmailCodeController;
import com.ajaxjs.iam.server.common.UserUtils;
import com.ajaxjs.iam.server.common.langs.LanguageMapping;
import com.ajaxjs.iam.server.model.AppSecretMgr;
import com.ajaxjs.iam.server.model.UserAccountType;
import com.ajaxjs.iam.server.service.ClientCredential;
import com.ajaxjs.message.email.Email;
import com.ajaxjs.message.email.resend.Resend;
import com.ajaxjs.sqlman.Action;
import com.ajaxjs.util.ObjectHelper;
import com.ajaxjs.util.RandomTools;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.date.DateTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
@Slf4j
public class EmailCodeService implements EmailCodeController {
    private final Cache<String, Object> cache = ExpiryCache.getInstance();

    @Override
    public boolean sendVerificationCode(String email) {
        String appId = ClientCredential.getAppId();

        if (!StringUtils.hasText(email) || !UserUtils.isValidEmail(email)) // 请提交有效的邮箱
            throw new IllegalArgumentException(LanguageMapping.getLanguageByKey("sms.phone.invalid"));

        AppSecretMgr appSecretMgr = new Action("SELECT app_id, app_secret FROM app_secret_mgr WHERE owner = ?").query(appId).one(AppSecretMgr.class);

        if (appSecretMgr == null) // 请提供有效的 App 信息
            throw new NullPointerException(LanguageMapping.getLanguageByKey("sms.phone.provideAppInfo"));

//        String randCode = RandomTools.generateRandomString(6);
        int randCode = RandomTools.generateNumber(4);
        log.info("发送验证码：{} 到邮箱 {}", randCode, email);

        cache.put(email, randCode, 60 * 10);

        return sendEmail(randCode + "", email);
    }

    private boolean sendEmail(String randCode, String email) {
        String subject = CODE_EMAIL_TITLE_ENG + randCode;

        Map<String, Object> params = ObjectHelper.mapOf("username", email, "product", "Wyndme", "min", "10");
        params.put("code", randCode);
        params.put("time", DateTools.nowShort());
        String content = StrUtil.simpleTpl(CODE_EMAIL_ENG, params);

        Email emailEntity = new Email().setFrom("wyndme@gz88.cc").setTo(email).setSubject(subject).setContent(content);

        return new Resend().setApiKey(resendKey).sendEmail(emailEntity);
    }

    @Value("${aj-framework.message.email.resend.apikey}")
    private String resendKey;

//    public boolean sendEmailCode() {
//        Email emailEntity = new Email().setFrom("onboarding@resend.dev").setTo("sp42@qq.com").setSubject("测试邮件").setContent("测试邮件");
//        return new Resend().setApiKey(resendKey).sendEmail(emailEntity);
//    }

    @Autowired
    private JWebTokenMgr jWebTokenMgr;

    @Value("${User.oidc.jwtExpireHours:74}")
    private int jwtExpireHours;

    /**
     * Token 的有效期，单位：分钟  默认两天
     */
    @Value("${oauth.token.client_expires: 60}")
    private Integer clientExpires;

    @Override
    public JwtAccessToken verifyCode(String email, String code) {
        String appId = ClientCredential.getAppId();

        if (!StringUtils.hasText(email) || !UserUtils.isValidEmail(email)) // 请提交有效的邮箱
            throw new IllegalArgumentException(LanguageMapping.getLanguageByKey("sms.phone.invalid"));

        if (!StringUtils.hasText(code) || !code.matches("\\d{4}"))// 验证码非法
            throw new IllegalArgumentException(LanguageMapping.getLanguageByKey("sms.phone.invalid_verification_code"));

        Integer i = cache.get(email, Integer.class);

        if (i != null && i.equals(Integer.parseInt(code))) {
            cache.remove(email);

            LoginOrRegister loginOrRegister = new LoginOrRegister(UserAccountType.EMAIL, jWebTokenMgr, jwtExpireHours);
            loginOrRegister.setClientExpires(clientExpires);

            return loginOrRegister.createUser(email, appId);
        } else
            throw new SecurityException(LanguageMapping.getLanguageByKey("sms.phone.error_verification_code"));// 验证码错误
    }

    @Override
    public boolean updateUserEmail(String email, String appId, String code) {
        return false;
    }

    final static String CODE_EMAIL_TITLE_ZH = "Wyndme 验证码：";

    final static String CODE_EMAIL_ZH = """
            尊敬的 ${username}，
                感谢您使用 ${product}。
                我们收到了您的账户 [注册 / 登录] 请求。您的验证码为：
            
                ${code}
            
                该验证码将在 ${min} 分钟后失效。为了保障您的账户安全，请勿将验证码泄露给他人。
            
                如果您并未发起此请求，请忽略此邮件。您的账户依然是安全的。
            
                此致，
                    ${product} 团队
                    ${time}
            """;

    final static String CODE_EMAIL_TITLE_ENG = "Wyndme Verification Code:";

    final static String CODE_EMAIL_ENG = """
            Dear user ${username},<br />
                &nbsp;&nbsp;&nbsp;&nbsp;Thank you for using ${product}.<br />
                &nbsp;&nbsp;&nbsp;&nbsp;We received a request to [sign up / log in] to your account. Your verification code is:<br /><br />
            
                &nbsp;&nbsp;&nbsp;&nbsp;<b>${code}</b><br /><br />
            
                &nbsp;&nbsp;&nbsp;&nbsp;This code will expire in ${min} minutes. For your account security, please do not share this code with anyone.<br />
                &nbsp;&nbsp;&nbsp;&nbsp;If you did not initiate this request, please ignore this email. Your account remains secure.<br /><br />
            
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;    Best regards,<br />
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;    The ${product} Team<br />
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;    ${time}
            """;
//    public static void main(String[] args) {
//        Map<String, Object> params = ObjectHelper.mapOf("username", "zx", "product", "Wyndme", "min", "15");
//        params.put("code", "sdsdds");
//        params.put("time", DateTools.nowShort());
//
//        String tpl = StrUtil.simpleTpl(CODE_EMAIL_ENG, params);
//        System.out.println(tpl);
//    }
}
