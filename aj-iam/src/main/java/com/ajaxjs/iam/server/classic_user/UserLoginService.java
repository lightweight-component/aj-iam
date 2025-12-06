package com.ajaxjs.iam.server.classic_user;

import com.ajaxjs.framework.model.BusinessException;
import com.ajaxjs.iam.UserConstants;
import com.ajaxjs.iam.server.common.UserUtils;
import com.ajaxjs.iam.server.common.session.UserSession;
import com.ajaxjs.iam.server.model.User;
import com.ajaxjs.iam.server.service.LogLoginService;
import com.ajaxjs.iam.server.service.TenantService;
import com.ajaxjs.sqlman.Action;
import com.ajaxjs.util.CommonConstant;
import com.ajaxjs.util.ObjectHelper;
import com.ajaxjs.util.RandomTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.function.Function;

@Service
@Slf4j
public class UserLoginService implements UserLoginController {
    @Autowired
    UserSession userSession;

    @Override
    public Boolean isLogin() {
        return userSession.getUserFromSession() != null;
    }

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
            UserUtils.send303Redirect(resp, returnUrl);

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

        if (UserUtils.testBCD(UserConstants.LoginIdType.PSW_LOGIN_EMAIL, loginIdType) && UserUtils.isValidEmail(loginId))
            sql = String.format(sql, "email");
        else if (UserUtils.testBCD(UserConstants.LoginIdType.PSW_LOGIN_PHONE, loginIdType) && UserUtils.isValidPhone(loginId))
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
}
