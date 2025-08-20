package com.ajaxjs.iam.user.service.resetpsw;

import com.ajaxjs.iam.user.common.util.CheckStrength;
import com.ajaxjs.iam.user.model.User;
import com.ajaxjs.iam.user.model.UserAccount;
import com.ajaxjs.message.email.Email;
import com.ajaxjs.message.email.ISendEmail;
import com.ajaxjs.sqlman.Sql;
import com.ajaxjs.sqlman.crud.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.function.Function;

public abstract class BaseResetPasswordService {
    public static User findUserBy(String type, Object value, Integer tenantId) {
        String sql = "SELECT * FROM user WHERE %s = ? AND stat != 1";
        sql = String.format(sql, type);
        User user;

        if (tenantId != null && tenantId != 0) {
            sql += " AND tenant_id = ?";
            user = Sql.newInstance().input(sql, value, tenantId).query(User.class);
        } else
            user = Sql.newInstance().input(sql, value).query(User.class);

        if (user == null)
            throw new IllegalAccessError("该 " + type + ": " + value + " 的用户不存在！");

        return user;
    }

    @Autowired
    ISendEmail send;

    /**
     * 发送邮件
     */
    public boolean sendEmail(String to, String subject, String content) {
        return send.sendEmail(new Email().setTo(to).setFrom("onboarding@resend.dev").setSubject(subject).setContent(content));
    }

    @Autowired
    @Qualifier("passwordEncode")
    Function<String, String> passwordEncode;

    @Value("${auth.user.CheckStrength:true}")
    boolean isCheckPasswordStrength;

    /**
     * 更新用户密码
     *
     * @param user        用户信息
     * @param newPassword 用户输入的新密码
     * @return 是否修改成功
     */
    public boolean updatePwd(Map<String, Object> user, String newPassword) {
        // 检测密码强度
        if (isCheckPasswordStrength) {
            CheckStrength.LEVEL passwordLevel = CheckStrength.getPasswordLevel(newPassword); // 检测密码强度

            if (passwordLevel == CheckStrength.LEVEL.EASY)
                throw new UnsupportedOperationException("密码强度太低");
        }

        newPassword = passwordEncode.apply(newPassword);

        if (newPassword.equalsIgnoreCase(user.get("password").toString()))
            throw new UnsupportedOperationException("新密码与旧密码一致，没有修改");

        UserAccount updateAuth = new UserAccount();
        updateAuth.setId(Long.parseLong(String.valueOf(user.get("authId"))));
        updateAuth.setPassword(newPassword);

        return Entity.newInstance().input(updateAuth).update().isOk();
    }
}
