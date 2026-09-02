package com.ajaxjs.iam.server.auth;

import com.ajaxjs.framework.cache.Cache;
import com.ajaxjs.framework.database.EnableTransaction;
import com.ajaxjs.framework.model.BusinessException;
import com.ajaxjs.iam.UserConstants;
import com.ajaxjs.iam.client.BaseOidcClientUserController;
import com.ajaxjs.iam.jwt.JwtToken;
import com.ajaxjs.iam.model.App;
import com.ajaxjs.iam.server.auth.controller.UserLoginController;
import com.ajaxjs.iam.server.authorizatio.OAuthService;
import com.ajaxjs.iam.server.common.IamConstants;
import com.ajaxjs.iam.server.common.UserUtils;
import com.ajaxjs.iam.server.common.session.UserSession;
import com.ajaxjs.iam.server.model.User;
import com.ajaxjs.iam.server.service.ClientCredential;
import com.ajaxjs.iam.server.service.LogLoginService;
import com.ajaxjs.iam.server.service.TenantService;
import com.ajaxjs.iam.server.service.token.JwtTokenService;
import com.ajaxjs.spring.DiContextUtil;
import com.ajaxjs.sqlman.Action;
import com.ajaxjs.util.CommonConstant;
import com.ajaxjs.util.ObjectHelper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@Slf4j
public class UserLoginService implements UserLoginController, IamConstants {
    @Autowired
    UserSession userSession;

    @Autowired(required = false)
    Cache<String, Object> cache;

    @Override
    public void authorizationCode(String responseType, String clientId, String redirectUri, String scope, String state, String webUrl, HttpServletRequest req, HttpServletResponse resp) {
        OAuthService.sendAuthCode(userSession, responseType, clientId, redirectUri, scope, state, webUrl, req, resp, cache);
    }

    @Override
    public JwtToken token(String authorization, String grantType, String code, String state, String webUrl) {
        if (!"authorization_code".equals(grantType))
            throw new IllegalArgumentException("参数 grant_type 只能是 authorization_code");

        User user = cache.get(code + ":user", User.class);

        // 如果能够通过 Authorization Code 获取到对应的用户信息，则说明该 Authorization Code 有效
        if (user == null)
            throw new IllegalArgumentException("非法 code：" + code);

        App app = ClientCredential.getAppByAuthHeader(authorization);
        JwtTokenService tokenService = new JwtTokenService(app, user);
        JwtToken token = tokenService.create();
        tokenService.createSave(token);

        saveTokenToCache(user, token);
        cache.remove(code + ":user");  // 删除缓存

        return token;
    }

    @Override
    public boolean isLogined() {
        return userSession.getUserFromSession() != null;
    }

    @Data
    public static class TokenUser {
        Long userId;

        JwtToken accessToken;
    }

    /**
     * 保存 token 在缓存
     */
    void saveTokenToCache(User user, JwtToken token) {
        TokenUser tokenUser = new TokenUser();
        tokenUser.setUserId(user.getId());
        tokenUser.setAccessToken(token);

        String key = JWT_TOKEN_USER_KEY + "-" + token.getToken();
        cache.put(key, tokenUser, token.getExpiresIn() * 1000);// 将秒转换为毫秒作为过期时间返回
        log.info("Save user {} to cache, key: {}", tokenUser, key);
    }

    @Override
    @EnableTransaction
    public JwtToken refreshToken(String grantType, String refreshToken) {
        if (!GrantType.REFRESH_TOKEN.equals(grantType))
            throw new IllegalArgumentException("GrantType must be 'refresh_token'.");

        String appId = ClientCredential.getAppId();
        App app = ClientCredential.getApp(appId);

        return new JwtTokenService(app).refreshToken(refreshToken);
    }

    @Override
    public boolean login(String username, String password, String appId) {
        return false;
    }

    @Autowired
    LogLoginService logLoginService;

    @Override
    public JwtToken loginWeb(String username, String password, String appId) {
        return login(username, password, ClientCredential.getApp(appId));
    }

    private JwtToken login(String username, String password, App app) {
        return login(username, password, app, null);
    }

    private JwtToken login(String username, String password, App app, String tenantCode) {
        Integer tenantId;

        if (ObjectHelper.isEmptyText(tenantCode)) {
            tenantId = app.getTenantId();

            if (tenantId == null)
                tenantId = TenantService.getTenantId(false);
        } else
            tenantId = new Action("SELECT id FROM tenant WHERE code = ?").query(tenantCode).oneValue(Integer.class);

        if (tenantId == null)
            throw new IllegalArgumentException("找不到租户 id");

        User user = getUserLoginByPassword(username, password, tenantId);

        JwtTokenService tokenService = new JwtTokenService(app, user);
        JwtToken token = tokenService.create();
        tokenService.createSave(token);

        saveTokenToCache(user, token);

        BaseOidcClientUserController.setTokenToCookie(token, DiContextUtil.getResponse());
        logLoginService.saveLoginLog(user, DiContextUtil.getRequest());

        return token;
    }

    @Override
    public JwtToken loginByClient(String username, String password, String tenantCode) {
        App app = ClientCredential.getApp(ClientCredential.getAppId());// TODO just clientId?

        return login(username, password, app, tenantCode);
    }

    @Value("${user.loginIdType:3}")
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

        String sql = "SELECT u.* FROM user u INNER JOIN user_account a ON a.user_id = u.id " +
                "WHERE a.type = 'PASSWORD' AND u.stat != 1 AND u.%s = ? AND a.password = ? AND u.tenant_id = ?";

        log.info("loginType:{}, isPass: {}", loginIdType, UserUtils.testBCD(UserConstants.LoginIdType.PSW_LOGIN_EMAIL, loginIdType));

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

        log.info("{} 登录成功！", user.getName());

        return user;
    }

    @Override
    public JwtToken ropcToken(String grantType, String username, String password, String clientId, String clientSecret, String scope) {
        if (!"password".equals(grantType))
            throw new IllegalArgumentException("参数 grant_type 只能是 password");

        App app = ClientCredential.getApp(clientId, clientSecret);
        Integer tenantId = app.getTenantId();
        User user = getUserLoginByPassword(username, password, tenantId);

        JwtTokenService tokenService = new JwtTokenService(app, user);
        JwtToken token = tokenService.create();
        tokenService.createSave(token);

        return token;
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
            // TODO
        }

        return true;
    }
}
