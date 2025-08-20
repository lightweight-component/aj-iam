package com.ajaxjs.iam.user.service.resetpsw;

import com.ajaxjs.framework.cache.Cache;
import com.ajaxjs.iam.user.common.UserUtils;
import com.ajaxjs.iam.user.model.User;
import com.ajaxjs.iam.user.service.TenantService;
import com.ajaxjs.util.RandomTools;
import com.ajaxjs.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class ResetPasswordBySmsCode  extends BaseResetPasswordService implements ByCode {
    @Autowired
    @Qualifier("localCache")
    Cache<String, Object> cache;

    /**
     * 缓存的前缀
     */
    private final static String CACHE_PREFIX = "reset_pwd:sms:";

    /**
     * 短信代码有效时间，当前是 5 分钟
     */
    private final static int EXPIRE_SECONDS = 5 * 60;

    @Override
    public boolean sendCode(String phone) {
        if (!StringUtils.hasText(phone) || !UserUtils.isValidPhone(phone))
            throw new IllegalArgumentException("请提交有效的手机");

        Integer tenantId = TenantService.getTenantId();
        User user = BaseResetPasswordService.findUserBy("phone", phone, tenantId);
        String code = saveCode(user, phone, tenantId);
//        sendSMS.send(phone, code);

        return true;
    }

    @Override
    public String saveCode(User user, String phone, Integer tenantId) {
        String key = CACHE_PREFIX + phone + "_" + tenantId;
        String code;
        String radAndUserId = cache.get(key, String.class);

        if (StrUtil.hasText(radAndUserId)) {
            String[] _radAndUserId = radAndUserId.replace(CACHE_PREFIX, "").split("_");

            code = _radAndUserId[0];
        } else {
            code = RandomTools.generateRandomString(4).toUpperCase(); // 4 位随机码
            Long userId = user.getId();
            String loginId = user.getLoginId();
            radAndUserId = code + "_" + userId + "_" + loginId;

            cache.put(key, radAndUserId, EXPIRE_SECONDS);

            log.info("保存用户[{}] 手机[{}] 验证码[{}] 缓存成功", loginId, phone, code);
            /*
             * 服务端暂存手机号码，那么客户端就不用重复提供了。 验证验证码的时候，根据 userId 查找 手机号码，再得到验证码
             */
        }

        return code;
    }

    @Override
    public boolean verifyCodeUpdatePsw(String code, String newPsw, String phone) {
        if (!StringUtils.hasText(code))
            throw new IllegalArgumentException("请输入验证码");

        if (!StringUtils.hasText(phone) || !UserUtils.isValidPhone(phone))
            throw new IllegalArgumentException("请提交有效的手机");

        return _verifyCodeUpdatePsw(cache, code, CACHE_PREFIX, phone, newPsw);
    }
}
