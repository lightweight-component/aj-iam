package com.ajaxjs.iam.user.service;


import com.ajaxjs.framework.database.EnableTransaction;
import com.ajaxjs.framework.model.BusinessException;
import com.ajaxjs.iam.UserConstants;
import com.ajaxjs.iam.server.common.IamUtils;
import com.ajaxjs.iam.user.common.UserUtils;
import com.ajaxjs.iam.user.common.session.UserSession;
import com.ajaxjs.iam.user.common.util.CheckStrength;
import com.ajaxjs.iam.user.controller.UserLoginRegisterController;
import com.ajaxjs.iam.user.model.User;
import com.ajaxjs.iam.user.model.UserAccount;
import com.ajaxjs.security.iplist.IpList;
import com.ajaxjs.spring.DiContextUtil;
import com.ajaxjs.sqlman.Action;
import com.ajaxjs.sqlman.util.SnowflakeId;
import com.ajaxjs.sqlman.util.Utils;
import com.ajaxjs.util.CommonConstant;
import com.ajaxjs.util.ObjectHelper;
import com.ajaxjs.util.RandomTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.function.Function;

@Service
@Slf4j
public class UserLoginRegisterService implements UserLoginRegisterController, UserConstants {
    @Override
    public Boolean isLogin() {
        return userSession.getUserFromSession() != null;
    }

    @Autowired
    UserSession userSession;

    @Autowired
    LogLoginService logLoginService;

    @Override
//    @GoogleCaptchaCheck
    public boolean login(String loginId, String password, String returnUrl, HttpServletRequest req, HttpServletResponse resp) {
        Integer tenantId;

        if ("admin".equals(loginId) && TenantService.getTenantId() == null) // 超级管理员不属于任何租户
            tenantId = 0;
        else
            tenantId = TenantService.getTenantId();

        User user = getUserLoginByPassword(loginId, password, tenantId);

        // 会员登录之后的动作，会保存 userId 和 userName 在 Session 中
        userSession.put(UserSession.SESSION_KEY + user.getId() + "-" + RandomTools.generateRandomString(4), user); // 同一个用户多端登录，加随机码区分
//        session.setAttribute("userGroupId", user.getRoleId());// 获取资源权限总值

//        if (user.getRoleId() == null || user.getRoleId() == 0L) {
//            // 未设置用户权限
//        } else {
////			long privilegeTotal = DAO.getPrivilegeByUserGroupId(user.getRoleId());
////			LOGGER.info("获取用户权限 privilegeTotal:" + privilegeTotal);
////			sess.setAttribute("privilegeTotal", privilegeTotal);
//        }
        logLoginService.saveLoginLog(user, req);

        if (StringUtils.hasText(returnUrl))
            IamUtils.send303Redirect(resp, returnUrl);

        return true;
    }

    @Value("${user.loginIdType:1}")
    int loginIdType;

    @Autowired
    @Qualifier("passwordEncode")
    Function<String, String> passwordEncode;

    /**
     * 密码支持帐号、邮件、手机作为身份凭证
     */
    public User getUserLoginByPassword(String loginId, String password, Integer tenantId) {
        loginId = loginId.trim();
        password = password.trim();

        String sql = "SELECT u.* FROM user u INNER JOIN user_account a ON a.user_id = u.id WHERE u.stat != 1 AND u.%s = ? AND a.password = ? AND u.tenant_id = ?";

        if (UserUtils.testBCD(LoginIdType.PSW_LOGIN_EMAIL, loginIdType) && UserUtils.isValidEmail(loginId))
            sql = String.format(sql, "email");
        else if (UserUtils.testBCD(LoginIdType.PSW_LOGIN_PHONE, loginIdType) && UserUtils.isValidPhone(loginId))
            sql = String.format(sql, "phone");
        else
            sql = String.format(sql, "login_id");

        String encodePsw = passwordEncode.apply(password);
        User user = new Action(sql).query(loginId, encodePsw, tenantId).one(User.class);

        if (user == null)
            throw new BusinessException("用户 " + loginId + " 登录失败，用户不存在或密码错误");

        log.info(user.getName() + " 登录成功！");

        return user;
    }

    @Override
    public boolean logout(String returnUrl, HttpServletResponse resp, HttpSession session) {
        session.invalidate(); // 销毁会话
        // 清除 HttpOnly Cookie
        Cookie cookie = new Cookie(UserConstants.ACCESS_TOKEN_KEY, CommonConstant.EMPTY_STRING);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);

        if (ObjectHelper.hasText(returnUrl)) {

        }

        return true;
    }

    @Value("${auth.user.CheckStrength:true}")
    boolean isCheckPasswordStrength;

    @Override
    @EnableTransaction
    public Boolean register(Map<String, Object> params) {
        // 所有字符串 trim 一下
        for (String key : params.keySet()) {
            Object obj = params.get(key);

            if (obj instanceof String)
                params.put(key, obj.toString().trim());
        }

        // 校验
        int tenantId;

        if (!isNull(params, "tenantId"))
            tenantId = Integer.parseInt(params.get("tenantId").toString());
        else {
            tenantId = TenantService.getTenantId() == null ? 0 : TenantService.getTenantId();
            params.put("tenantId", tenantId);
        }

        if (tenantId == 0)
            throw new IllegalArgumentException("租户 id 不能为空");

        if (isNull(params, "password"))
            throw new IllegalArgumentException("注册密码不能为空");

        boolean hasNoUsername = isNull(params, "loginId"), hasNoEmail = isNull(params, "email"), hasNoPhone = isNull(params, "phone");
        if (hasNoUsername && hasNoEmail && hasNoPhone)
            throw new IllegalArgumentException("没有用户标识， loginId/email/phone 至少填一种");

        // 是否重复
        if (!hasNoUsername && isRepeat("login_id", params.get("loginId").toString(), tenantId))
            throw new IllegalArgumentException("用户名 loginId: " + params.get("loginId").toString() + " 重复");

        if (!hasNoEmail && isRepeat("email", params.get("email").toString(), tenantId))
            throw new IllegalArgumentException("邮箱: " + params.get("email").toString() + " 重复");

        if (!hasNoPhone && isRepeat("phone", params.get("phone").toString(), tenantId))
            throw new IllegalArgumentException("手机: " + params.get("phone").toString() + " 重复");

        // 获取业务自定义的字段，存在 extract json 中
        Map<String, Object> extract = new HashMap<>();
        List<String> basicFields = Arrays.asList("loginId", "email", "phone", "password", "tenantId");
//        final Map<String, Object> _params = new HashMap<>(params);
//
//        _params.forEach((key, value) -> {
//            if (!basicFields.contains(key)) {
//                _params.remove(key);
//                extract.put(key, value);
//            }
//        });

        params.entrySet().removeIf(entry -> {
            String key = entry.getKey();
            Object value = entry.getValue();
            boolean isRemove = !basicFields.contains(key);

            if (isRemove && value != null && (value instanceof String && ObjectHelper.hasText(value.toString())))
                extract.put(key, value);

            return isRemove;
        });

        if (!ObjectUtils.isEmpty(extract))
            params.put("extend", extract);

        System.out.println(extract);

        // 有些字段不要
        String psw = params.get("password").toString();
        params.remove("password");

        // 检测密码强度
        if (isCheckPasswordStrength) {
            CheckStrength.LEVEL passwordLevel = CheckStrength.getPasswordLevel(psw);

            if (passwordLevel == CheckStrength.LEVEL.EASY)
                throw new UnsupportedOperationException("密码强度太低");
        }

        params = Utils.changeFieldToColumnName(params);
        params.put("uid", SnowflakeId.get());
        params.put("bindState", UserFunction.BindState.IAM);

        long userId = new Action(params, "user").create().execute(true, Long.class).getNewlyId(); // 写入数据库

        saveUserRole(userId, tenantId);

        UserAccount auth = new UserAccount();
        auth.setUserId(userId);
        auth.setPassword(passwordEncode.apply(psw));
        auth.setRegisterType(LoginType.PASSWORD);
        auth.setRegisterIp(IpList.getClientIp(Objects.requireNonNull(DiContextUtil.getRequest())));

        return new Action(auth, "user_account").create().execute(true, Long.class).isOk();
    }

    /**
     * 保存用户角色
     */
    private static void saveUserRole(long userId, int tenantId) {
        String sql = "INSERT INTO per_user_role (user_id, role_id)\n" +
                "(SELECT ?, default_role_id FROM tenant WHERE id = ? AND default_role_id IS NOT NULL)";
        // default_role_id 如果为空则不插入新数据（用户没角色）
        boolean isOk = new Action(sql).create(userId, tenantId).execute(true, Long.class).isOk();

        if (isOk)
            log.info("保存用户角色成功！");
        else
            log.warn("保存用户角色失败！");
    }

    private static boolean isNull(Map<String, Object> params, String key) {
        return !params.containsKey(key) || !StringUtils.hasText(params.get(key).toString());
    }

    @Override
    public Boolean checkRepeat(String field, String value) {
        return isRepeat(field, value, TenantService.getTenantId());
    }

    /**
     * 检查某个值是否已经存在一样的值
     *
     * @param field 数据库里面的字段名称
     * @param value 欲检查的值
     * @return true=值重复
     */
    public static boolean isRepeat(String field, String value, int tenantId) {
        String sql = "SELECT id FROM user WHERE stat != 1 AND %s = ? AND tenant_id = ? LIMIT 1";
        sql = String.format(sql, field.trim());

        return new Action(sql).query(value.trim(), tenantId).oneValue(Long.class) != null; // 有这个数据表示重复
    }
}
