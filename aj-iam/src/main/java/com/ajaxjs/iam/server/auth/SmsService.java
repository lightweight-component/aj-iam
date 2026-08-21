package com.ajaxjs.iam.server.auth;

import com.ajaxjs.framework.cache.Cache;
import com.ajaxjs.framework.cache.delayqueue.ExpiryCache;
import com.ajaxjs.iam.client.SecurityManager;
import com.ajaxjs.iam.jwt.JwtToken;
import com.ajaxjs.iam.server.auth.apponekeylogin.AliyunOpenApi;
import com.ajaxjs.iam.server.auth.apponekeylogin.AliyunSmsEntity;
import com.ajaxjs.iam.server.auth.apponekeylogin.LoginOrRegister;
import com.ajaxjs.iam.server.auth.apponekeylogin.SendAliyunSms;
import com.ajaxjs.iam.server.auth.controller.SmsController;
import com.ajaxjs.iam.server.common.UserUtils;
import com.ajaxjs.iam.server.common.langs.LanguageMapping;
import com.ajaxjs.iam.server.common.session.UserSession;
import com.ajaxjs.iam.server.model.AppSecretMgr;
import com.ajaxjs.iam.server.model.User;
import com.ajaxjs.iam.server.model.UserAccountType;
import com.ajaxjs.iam.server.service.ClientCredential;
import com.ajaxjs.iam.server.service.TenantService;

import com.ajaxjs.spring.DiContextUtil;
import com.ajaxjs.sqlman.Action;
import com.ajaxjs.util.RandomTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * SMS
 */
@Slf4j
@Service
public class SmsService implements SmsController {
    @Autowired
    AliyunOpenApi aliyunOpenApi;

    @Override
    public JwtToken mobileAppOneKeyLoginAli(String token) {
        String appId = ClientCredential.getAppId();
        String phone = aliyunOpenApi.getPhoneByToken(token);
        log.info("已获取手机号码 {}", phone);

        return new LoginOrRegister(UserAccountType.PHONE).createUser(phone, appId);
    }

    private final Cache<String, Object> cache = ExpiryCache.getInstance();

    @Override
    public boolean sendVerificationCode(String phone) {
        String appId = ClientCredential.getAppId();

        return sendSms(appId, phone);
    }

    boolean sendSms(String appId, String phone) {
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
    public boolean sendVerificationCodeWeb(String phone, String smsClientId) {
        return sendSms(smsClientId, phone);
    }

    @Autowired
    UserSession userSession;

    @Override
    public boolean loginBySms(String phone, String code) {
        if (!StringUtils.hasText(phone) || !UserUtils.isValidPhone(phone))
            throw new IllegalArgumentException(LanguageMapping.getLanguageByKey("sms.phone.invalid"));

        if (!StringUtils.hasText(code) || !code.matches("\\d{4}"))// 验证码非法
            throw new IllegalArgumentException(LanguageMapping.getLanguageByKey("sms.phone.invalid_verification_code"));

        Integer i = cache.get(phone, Integer.class);

        if (i != null && i.equals(Integer.parseInt(code))) {
            cache.remove(phone);

            Integer tenantId = TenantService.getTenantId(false);

            if (tenantId == null)
                throw new IllegalArgumentException("请选择租户");

            User user = new Action("SELECT * FROM user WHERE stat = 0 AND phone = ? AND tenant_id = ?").query(phone, tenantId).one(User.class);

            if (user == null)
                throw new NullPointerException("用户 " + phone + " 不存在");

            log.info("保存用户到 session");
            userSession.put(UserSession.SESSION_KEY, user);
//            log.info("user in session:" + userSession.getUserFromSession());
            log.info("session "+ DiContextUtil.getSession());

            return true;
        } else
            throw new SecurityException(LanguageMapping.getLanguageByKey("sms.phone.error_verification_code"));// 验证码错误
    }

    @Override
    public JwtToken verifyCode(String phone, String code) {
        String appId = ClientCredential.getAppId();

        if (!StringUtils.hasText(phone) || !UserUtils.isValidPhone(phone))
            throw new IllegalArgumentException(LanguageMapping.getLanguageByKey("sms.phone.invalid"));

        if (!StringUtils.hasText(code) || !code.matches("\\d{4}"))// 验证码非法
            throw new IllegalArgumentException(LanguageMapping.getLanguageByKey("sms.phone.invalid_verification_code"));

        Integer i = cache.get(phone, Integer.class);

        if (i != null && i.equals(Integer.parseInt(code))) {
            cache.remove(phone);

            return new LoginOrRegister(UserAccountType.PHONE).createUser(phone, appId);
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
            Long userId = SecurityManager.getUser().getId();// update user info

            return new Action("UPDATE user SET phone = ? WHERE id = ?").update(phone, userId).execute().isOk();
        } else
            throw new SecurityException(LanguageMapping.getLanguageByKey("sms.phone.error_verification_code"));
    }

    @Override
    public boolean test() {
        User user = new User();
        user.setId(25L);
        user.setLoginId("admin");
        user.setUsername("Jack");
        user.setTenantId(9L);

        log.info("保存用户到 session");
        userSession.put(UserSession.SESSION_KEY, user);
        log.info("user in session:" + userSession.getUserFromSession());

        log.info("session "+ DiContextUtil.getSession().getId());
        log.info("session "+ DiContextUtil.getSession().getAttribute(UserSession.SESSION_KEY));

        return true;
    }
}
