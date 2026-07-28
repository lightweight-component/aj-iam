package com.ajaxjs.iam.server.auth;

import com.ajaxjs.framework.cache.Cache;
import com.ajaxjs.framework.database.EnableTransaction;
import com.ajaxjs.framework.model.BusinessException;
import com.ajaxjs.iam.UserConstants;
import com.ajaxjs.iam.client.BaseOidcClientUserController;
import com.ajaxjs.iam.jwt.JWebTokenMgr;
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
import com.ajaxjs.iam.server.service.token.model.JwtToken;
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

    @Autowired
    JWebTokenMgr jWebTokenMgr;

    @Override
    public void authorizationCode(String responseType, String clientId, String redirectUri, String scope, String state, String webUrl, HttpServletRequest req, HttpServletResponse resp) {
        OAuthService.sendAuthCode(userSession, responseType, clientId, redirectUri, scope, state, webUrl, req, resp, cache);
    }

    /**
     * Token 的有效期，单位：分钟  默认一天
     */
    @Value("${oauth.token.client_expires: 3600}")
    private Integer tokenExpires;

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
        tokenService.setjWebTokenMgr(jWebTokenMgr);
        tokenService.setTokenExpires(tokenExpires);
        JwtToken token = tokenService.create();
        tokenService.createSave(token);

        // 保存 token 在缓存
        saveTokenToCache(user, token);

        // 删除缓存
        cache.remove(code + ":user");

        return token;
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

        JwtTokenService jwtTokenService = new JwtTokenService(app);
        jwtTokenService.setjWebTokenMgr(jWebTokenMgr);
        jwtTokenService.setTokenExpires(tokenExpires);

        return jwtTokenService.refreshToken(refreshToken);
    }

    @Autowired
    LogLoginService logLoginService;

    @Override
    public JwtToken login(String username, String password, String appId) {
        App app = ClientCredential.getApp(appId);
        Integer tenantId = TenantService.getTenantId(false);

        if (tenantId == null || tenantId == 0) // for iam admin, no tenant id means iam admin
            tenantId = app.getTenantId();

        User user = getUserLoginByPassword(username, password, tenantId);

        JwtTokenService jwtTokenService = new JwtTokenService(app, user);
        jwtTokenService.setjWebTokenMgr(jWebTokenMgr);
        jwtTokenService.setTokenExpires(tokenExpires);
        JwtToken token = jwtTokenService.create();
        jwtTokenService.createSave(token);

        saveTokenToCache(user, token);

        BaseOidcClientUserController.setTokenToCookie(token, DiContextUtil.getResponse());
        logLoginService.saveLoginLog(user, DiContextUtil.getRequest());

        return token;
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
        tokenService.setjWebTokenMgr(jWebTokenMgr);
        tokenService.setTokenExpires(tokenExpires);
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
