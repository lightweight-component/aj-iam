package com.ajaxjs.iam.user.service.resetpsw;

import com.ajaxjs.framework.spring.SimpleTemplate;
import com.ajaxjs.iam.user.common.UserUtils;
import com.ajaxjs.iam.user.model.User;
import com.ajaxjs.iam.user.service.TenantService;
import com.ajaxjs.message.email.ISendEmail;
import com.ajaxjs.sqlman.Sql;
import com.ajaxjs.util.HashHelper;
import com.ajaxjs.util.UrlEncode;
import com.ajaxjs.util.cryptography.Cryptography;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ResetPasswordByEmailLink extends BaseResetPasswordService {
    @Value("${Website.basePath: }")
    private String websiteBasePath;

    /**
     *
     */
    private final static String FIND_BY_EMAIL = "/user/reset_password/findByEmail/";

    /**
     *
     */
    private final static int TOKEN_TIMEOUT = 20;

    @Value("${User.restPassword.encryptKey:d4X87f43}")
    private String encryptKey;

    /**
     * 邮件模板
     */
    // @formatter:off
    public final static String BY_LINK_HTML = "用户 ${username} 您好：<br />&nbsp;&nbsp;&nbsp;&nbsp;请点击下面的链接进行${desc}：<br />"
            + "&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"${link}\" target=\"_blank\">${link}</a>。"
            + "<br /> &nbsp;&nbsp;&nbsp;&nbsp;提示：1）请勿回复本邮件；2）本邮件超过 ${timeout} 小时的话链接将会失效，需要重新申请${desc}；3）如不能打开，请复制该链接到浏览器。";
    // @formatter:on

    @Autowired
    ISendEmail send;

    public boolean sendRestEmail(String email) {
        if (!StringUtils.hasText(email) || !UserUtils.isValidEmail(email))
            throw new IllegalArgumentException("请提交有效的邮件地址");

        Integer tenantId = TenantService.getTenantId();
        User user = findUserBy("email", email, tenantId);
        String token = makeEmailToken(email, tenantId);

        String url = websiteBasePath + FIND_BY_EMAIL + String.format("?email=%s&token=%s", new UrlEncode(email).encodeQuery(), new UrlEncode(token).encodeQuery());

        String title = "重置密码";
        Map<String, String> map = new HashMap<>();
        map.put("username", user.getLoginId());
        map.put("link", url);
        map.put("desc", title);
        map.put("timeout", String.valueOf(TOKEN_TIMEOUT));
        String content = SimpleTemplate.render(BY_LINK_HTML, map);

        return sendEmail(email, title, content);
    }

    /**
     * 生成重置密码的 Token（ for 邮件） 这 Token 在有效期内一直有效 TODO，令其无效。 该签名方法不能公开
     * <a href="https://www.cnblogs.com/shenliang123/p/3266770.html">...</a>
     * <a href="https://blog.wamdy.com/archives/1708.html">...</a>
     *
     * @param email    邮件地址
     * @param tenantId 租户 id
     * @return Token 签名
     */
    public String makeEmailToken(String email, Integer tenantId) {
        String expireHex = Long.toHexString(System.currentTimeMillis());
        String emailToken = HashHelper.getSHA1(encryptKey + email),
                timeToken = Cryptography.AES_encode(expireHex, encryptKey);

        return emailToken + timeToken;
    }

    public boolean verifyEmailUpdatePsw(String token, String newPsw, String email) {
        if (!checkEmailToken(token, email))
            throw new IllegalAccessError("校验 Token　失败");

        Integer tenantId = TenantService.getTenantId();
        User user = findUserBy("email", email, tenantId);

        Map<String, Object> _user = Sql.newInstance().input(
                "SELECT u.*, a.id AS auth_id, a.password FROM user u LEFT JOIN user_account a ON u.id = a.user_id WHERE u.id = ?", user.getId()).query();

        return updatePwd(_user, newPsw);
    }

    /**
     * 验证重置密码的 token 是否有效
     *
     * @param token 令牌
     * @param email 用户提交用于对比的 email
     * @return true = 通过
     */
    public boolean checkEmailToken(String token, String email) {
        String emailToken = token.substring(0, 40), timeToken = token.substring(40);

        if (!MessageDigestHelper.getSHA1(encryptKey + email).equals(emailToken))
            throw new SecurityException("非法 email 账号！ " + email);

        String expireHex = Cryptography.AES_decode(timeToken, encryptKey);
        long cha = new Date().getTime() - Long.parseLong(expireHex, 16);
        double result = cha * 1.0 / (1000 * 60 * 60);

        if (result <= TOKEN_TIMEOUT)
            return true;// 合法
        else
            throw new IllegalAccessError("该请求已经过期，请重新发起");
    }
}
