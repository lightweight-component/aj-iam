package com.ajaxjs.iam.server.service.apponekeylogin;

import com.ajaxjs.framework.cache.Cache;
import com.ajaxjs.framework.cache.delayqueue.ExpiryCache;
import com.ajaxjs.iam.client.SecurityManager;
import com.ajaxjs.iam.jwt.JwtAccessToken;
import com.ajaxjs.iam.server.common.UserUtils;
import com.ajaxjs.iam.server.controller.SmsController;
import com.ajaxjs.iam.server.model.AppSecretMgr;
import com.ajaxjs.iam.server.model.User;
import com.ajaxjs.iam.server.service.TenantService;
import com.ajaxjs.message.sms.ali_sms.AliyunSmsEntity;
import com.ajaxjs.sqlman.Action;
import com.ajaxjs.util.RandomTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * TODO 安全
 */
@Slf4j
@Service
public class SmsService implements SmsController {
    @Autowired
    LoginOrRegister loginOrRegister;

    @Override
    public JwtAccessToken mobileAppOneKeyLoginAli(String token) {
        return loginOrRegister.byPhone(token);
    }

    private final Cache<String, Object> cache = ExpiryCache.getInstance();

    @Override
    public boolean sendVerificationCode(String phone, String appId) {
        if (!StringUtils.hasText(phone) || !UserUtils.isValidPhone(phone))
            throw new IllegalArgumentException("请提交有效的手机");

        AppSecretMgr appSecretMgr = new Action("SELECT app_id, app_secret FROM app_secret_mgr WHERE owner = ?").query(appId).one(AppSecretMgr.class);

        if (appSecretMgr == null)
            throw new NullPointerException("Please provide the App information.");

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

        String result = SendAliyunSms2.send(entity);

        if (result.equals("OK"))
            return true;
        else
            throw new UnsupportedOperationException(result);
    }

    @Override
    public JwtAccessToken verifyCode(String phone, String appId, String code) {
        if (!StringUtils.hasText(phone) || !UserUtils.isValidPhone(phone))
            throw new IllegalArgumentException("请提交有效的手机");

        if (!StringUtils.hasText(code) || !code.matches("\\d{4}"))
            throw new IllegalArgumentException("验证码非法");

        Integer i = cache.get(phone, Integer.class);

        if (i != null && i.equals(Integer.parseInt(code))) {
            cache.remove(phone);

            return loginOrRegister.createUserByPhone(phone);
        } else
            throw new SecurityException("验证码错误");
    }

    @Override
    public boolean updateUserPhone(String phone, String appId, String code) {
        if (!StringUtils.hasText(phone) || !UserUtils.isValidPhone(phone))
            throw new IllegalArgumentException("请提交有效的手机");

        // 先判断目标手机号码是否已有用户
        Integer tenantId = TenantService.getTenantId(false);
        User existUser = new Action("SELECT * FROM user WHERE phone = ? AND tenant_id = ?").query(phone, tenantId).one(User.class);

        if (existUser != null)
            throw new UnsupportedOperationException("当前手机 " + phone + " 的用户已经注册。不支持修改该手机号码。");

        if (!StringUtils.hasText(code) || !code.matches("\\d{4}"))
            throw new IllegalArgumentException("验证码非法");

        Integer i = cache.get(phone, Integer.class);

        if (i != null && i.equals(Integer.parseInt(code))) {
            cache.remove(phone);

            // update user info
            Long userId = SecurityManager.getUser().getId();

            return new Action("UPDATE user SET phone = ? WHERE id = ?").update(phone, userId).execute().isOk();
        } else
            throw new SecurityException("验证码错误");
    }
}
