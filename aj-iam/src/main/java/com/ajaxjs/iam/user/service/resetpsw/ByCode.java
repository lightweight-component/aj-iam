package com.ajaxjs.iam.user.service.resetpsw;

import com.ajaxjs.framework.cache.Cache;
import com.ajaxjs.iam.user.model.User;
import com.ajaxjs.iam.user.service.TenantService;
import com.ajaxjs.sqlman.Action;
import org.springframework.util.StringUtils;

import java.util.Map;

public interface ByCode {
    boolean sendCode(String any);

    String saveCode(User user, String any, Integer tenantId);

    boolean verifyCodeUpdatePsw(String code, String newPsw, String any);

    boolean updatePwd(Map<String, Object> user, String newPassword);

    default boolean _verifyCodeUpdatePsw(Cache<String, Object> cache, String code, String CACHE_PREFIX, String any, String newPsw) {
        Integer tenantId = TenantService.getTenantId();
        String key = CACHE_PREFIX + any + "_" + tenantId;
        String radAndUserId = cache.get(key, String.class);

        if (!StringUtils.hasText(radAndUserId))
            throw new IllegalStateException("未发送邮件/短讯验证码或验证码已经失效。找不到用户[" + any + "]的验证码");

        String[] _radAndUserId = radAndUserId.replace(CACHE_PREFIX, "").split("_");
        String rad = _radAndUserId[0], userId = _radAndUserId[1];

        if (!rad.equalsIgnoreCase(code))
            throw new IllegalArgumentException("验证码不正确");

        cache.remove(key);// 验证码正确，删除缓存

        Map<String, Object> user = new Action(
                "SELECT u.*, a.id AS auth_id, a.password FROM user u LEFT JOIN user_account a ON u.id = a.user_id WHERE u.id = ?").query(userId).one();

        return updatePwd(user, newPsw);
    }
}
