package com.ajaxjs.iam.user.service.resetpsw;

import com.ajaxjs.framework.cache.Cache;
import com.ajaxjs.framework.spring.SimpleTemplate;
import com.ajaxjs.iam.user.common.UserUtils;
import com.ajaxjs.iam.user.model.User;
import com.ajaxjs.iam.user.service.TenantService;
import com.ajaxjs.message.email.Email;
import com.ajaxjs.message.email.ISendEmail;
import com.ajaxjs.util.ObjectHelper;
import com.ajaxjs.util.RandomTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
@Slf4j
public class ResetPasswordByEmailCode extends BaseResetPasswordService implements ByCode {
    @Autowired
    @Qualifier("localCache")
    Cache<String, Object> cache;

    @Autowired
    ISendEmail send;

    /**
     * 缓存的前缀
     */
    private final static String CACHE_PREFIX = "reset_pwd:email:";

    /**
     * 短信代码有效时间，当前是 5 分钟
     */
    private final static int EXPIRE_SECONDS = 5 * 60;

    @Override
    public boolean sendCode(String email) {
        if (!StringUtils.hasText(email) || !UserUtils.isValidEmail(email))
            throw new IllegalArgumentException("请提交有效的邮件地址");

        Integer tenantId = TenantService.getTenantId();
        User user = BaseResetPasswordService.findUserBy("email", email, tenantId);
        String code = saveCode(user, email, tenantId);

        String title = "重置密码";
        Map<String, Object> map = ObjectHelper.mapOf("username", user.getLoginId(), "desc", title, "timeout", EXPIRE_SECONDS / 60);
        map.put("code", code);
        String content = SimpleTemplate.render(BY_CODE_HTML, map);

        return send.sendEmail(new Email().setTo(email).setFrom("onboarding@resend.dev").setSubject(title).setContent(content));
    }

    @Override
    public String saveCode(User user, String email, Integer tenantId) {
        String key = CACHE_PREFIX + email + "_" + tenantId;
        String code;
        String radAndUserId = cache.get(key, String.class);

        if (ObjectHelper.hasText(radAndUserId)) {
            String[] _radAndUserId = radAndUserId.replace(CACHE_PREFIX, "").split("_");

            code = _radAndUserId[0];
        } else {
//            code = RandomTools.generateRandomString(6).toUpperCase(); // 6 位随机码
            code = String.valueOf(RandomTools.generateNumber(4));
            Long userId = user.getId();
            String loginId = user.getLoginId();
            radAndUserId = code + "_" + userId + "_" + loginId;

            cache.put(key, radAndUserId, EXPIRE_SECONDS);

            log.info("保存用户[{}] 邮件[{}] 验证码[{}] 缓存成功", loginId, email, code);
            /*
             * 服务端暂存手机号码，那么客户端就不用重复提供了。 验证验证码的时候，根据 userId 查找 手机号码，再得到验证码
             */
        }

        return code;
    }

    /**
     * 邮件模板
     */
    // @formatter:off
    final static String BY_CODE_HTML = "用户 ${username} 您好：<br /><br />&nbsp;&nbsp;&nbsp;&nbsp;${desc}的验证码如下：<br />"
            + "&nbsp;&nbsp;&nbsp;&nbsp;<div style=\"font-size:20px;font-weight:bold;letter-spacing:3px;padding: 0 15px;\">${code}</div>"
            + "<br /> &nbsp;&nbsp;&nbsp;&nbsp;提示：1）请勿回复本邮件；2）验证码超过 ${timeout} 分钟接将会失效，需要重新申请${desc}。";
    // @formatter:on

    @Override
    public boolean verifyCodeUpdatePsw(String code, String newPsw, String email) {
        if (!StringUtils.hasText(code))
            throw new IllegalArgumentException("请输入验证码");

        if (!StringUtils.hasText(email) || !UserUtils.isValidEmail(email))
            throw new IllegalArgumentException("请提交有效的邮件地址");

        return _verifyCodeUpdatePsw(cache, code, CACHE_PREFIX, email, newPsw);
    }
}
