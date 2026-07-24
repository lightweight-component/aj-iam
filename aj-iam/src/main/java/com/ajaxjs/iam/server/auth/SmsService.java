package com.ajaxjs.iam.server.auth;

import com.ajaxjs.framework.cache.Cache;
import com.ajaxjs.framework.cache.delayqueue.ExpiryCache;
import com.ajaxjs.iam.client.SecurityManager;
import com.ajaxjs.iam.jwt.JWebTokenMgr;
import com.ajaxjs.iam.jwt.JwtAccessToken;
import com.ajaxjs.iam.server.auth.apponekeylogin.AliyunOpenApi;
import com.ajaxjs.iam.server.auth.apponekeylogin.LoginOrRegister;
import com.ajaxjs.iam.server.auth.apponekeylogin.SendAliyunSms;
import com.ajaxjs.iam.server.auth.controller.SmsController;
import com.ajaxjs.iam.server.common.UserUtils;
import com.ajaxjs.iam.server.common.langs.LanguageMapping;
import com.ajaxjs.iam.server.model.AppSecretMgr;
import com.ajaxjs.iam.server.model.User;
import com.ajaxjs.iam.server.model.UserAccountType;
import com.ajaxjs.iam.server.service.ClientCredential;
import com.ajaxjs.iam.server.service.TenantService;
import com.ajaxjs.message.sms.ali_sms.AliyunSmsEntity;
import com.ajaxjs.sqlman.Action;
import com.ajaxjs.util.RandomTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 *
 */
@Slf4j
@Service
public class SmsService implements SmsController {
    @Autowired
    AliyunOpenApi aliyunOpenApi;

    @Autowired
    JWebTokenMgr jWebTokenMgr;

    @Value("${User.oidc.jwtExpireHours:74}")
    int jwtExpireHours;

    /**
     * Token 的有效期，单位：分钟  默认两天
     */
    @Value("${oauth.token.client_expires: 60}")
    private Integer clientExpires;

    @Override
    public JwtAccessToken mobileAppOneKeyLoginAli(String token) {
        String appId = ClientCredential.getAppId();
        String phone = aliyunOpenApi.getPhoneByToken(token);
        log.info("已获取手机号码 {}", phone);

        LoginOrRegister loginOrRegister = new LoginOrRegister(UserAccountType.PHONE, jWebTokenMgr, jwtExpireHours);
        loginOrRegister.setClientExpires(clientExpires);

        return loginOrRegister.createUser(phone, appId);
    }

    private final Cache<String, Object> cache = ExpiryCache.getInstance();

    @Override
    public boolean sendVerificationCode(String phone) {
        String appId = ClientCredential.getAppId();

        if (!StringUtils.hasText(phone) || !UserUtils.isValidPhone(phone)) // 请提交有效的手机
            throw new IllegalArgumentException(LanguageMapping.getLanguageByKey("sms.phone.invalid"));

        AppSecretMgr appSecretMgr = new Action("SELECT app_id, app_secret FROM app_secret_mgr WHERE owner = ?").query(appId).one(AppSecretMgr.class);

        if (appSecretMgr == null) // 请提供有效的 App 信息
            throw new NullPointerException(LanguageMapping.getLanguageByKey("sms.phone.provideAppInfo"));

        int randCode = RandomTools.generateNumber(4);
        String param = String.format("{\"code\":\"%s\",\"min\":\"5\"}", randCode);
        log.info("发送验证码：{}", param);
        log.info("setAccessKeyId {}", appSecretMgr.getAppId());
        log.info("setAccessSecret {}", appSecretMgr.getAppSecret());
        cache.put(phone, randCode, 60 * 5);

        AliyunSmsEntity entity = new AliyunSmsEntity();
        entity.setAccessKeyId(appSecretMgr.getAppId());
        entity.setAccessSecret(appSecretMgr.getAppSecret());
        entity.setSignName("速通互联验证码");
        entity.setTemplateCode("100001");
        entity.setTemplateParam(param);
        entity.setPhoneNumbers(phone);

        String result = SendAliyunSms.send(entity);

        if (result.equals("OK"))
            return true;
        else
            throw new UnsupportedOperationException(result);
    }

    @Override
    public JwtAccessToken verifyCode(String phone, String code) {
        String appId = ClientCredential.getAppId();

        if (!StringUtils.hasText(phone) || !UserUtils.isValidPhone(phone))
            throw new IllegalArgumentException(LanguageMapping.getLanguageByKey("sms.phone.invalid"));

        if (!StringUtils.hasText(code) || !code.matches("\\d{4}"))// 验证码非法
            throw new IllegalArgumentException(LanguageMapping.getLanguageByKey("sms.phone.invalid_verification_code"));

        Integer i = cache.get(phone, Integer.class);

        if (i != null && i.equals(Integer.parseInt(code))) {
            cache.remove(phone);

            LoginOrRegister loginOrRegister = new LoginOrRegister(UserAccountType.PHONE, jWebTokenMgr, jwtExpireHours);
            loginOrRegister.setClientExpires(clientExpires);

            return loginOrRegister.createUser(phone, appId);
        } else
            throw new SecurityException(LanguageMapping.getLanguageByKey("sms.phone.error_verification_code"));// 验证码错误
    }

    @Override
    public boolean updateUserPhone(String phone, String appId, String code) {
        if (!StringUtils.hasText(phone) || !UserUtils.isValidPhone(phone))
            throw new IllegalArgumentException(LanguageMapping.getLanguageByKey("sms.phone.invalid"));

        // 先判断目标手机号码是否已有用户
        Integer tenantId = TenantService.getTenantId(false);
        User existUser = new Action("SELECT * FROM user WHERE phone = ? AND tenant_id = ? AND stat != 1").query(phone, tenantId).one(User.class);

        if (existUser != null) // "当前手机 " + phone + " 的用户已经注册。不支持修改该手机号码。"
            throw new UnsupportedOperationException(String.format(LanguageMapping.getLanguageByKey("sms.phone.phone_exist"), phone));

        if (!StringUtils.hasText(code) || !code.matches("\\d{4}"))
            throw new IllegalArgumentException(LanguageMapping.getLanguageByKey("sms.phone.invalid_verification_code"));

        Integer i = cache.get(phone, Integer.class);

        if (i != null && i.equals(Integer.parseInt(code))) {
            cache.remove(phone);

            // update user info
            Long userId = SecurityManager.getUser().getId();

            return new Action("UPDATE user SET phone = ? WHERE id = ?").update(phone, userId).execute().isOk();
        } else
            throw new SecurityException(LanguageMapping.getLanguageByKey("sms.phone.error_verification_code"));
    }
}
