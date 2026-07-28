package com.ajaxjs.iam.server.authorizatio;

import com.ajaxjs.framework.cache.Cache;
import com.ajaxjs.framework.database.EnableTransaction;
import com.ajaxjs.framework.model.BusinessException;
import com.ajaxjs.iam.model.AccessToken;
import com.ajaxjs.iam.model.App;
import com.ajaxjs.iam.server.authorizatio.controller.OAuthController;
import com.ajaxjs.iam.server.common.IamConstants;
import com.ajaxjs.iam.server.common.UserUtils;
import com.ajaxjs.iam.server.common.session.UserSession;
import com.ajaxjs.iam.server.model.User;
import com.ajaxjs.iam.server.service.ClientCredential;
import com.ajaxjs.iam.server.service.TenantService;
import com.ajaxjs.iam.server.service.token.ClassicAccessToken;
import com.ajaxjs.sqlman.Action;
import com.ajaxjs.util.HashHelper;
import com.ajaxjs.util.ObjectHelper;
import com.ajaxjs.util.RandomTools;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class OAuthService implements OAuthController, IamConstants {
    @Autowired(required = false)
    private Cache<String, Object> cache;

    @Value("${oauth.token.user_expires: 120}")
    private Integer userExpires;

    @Autowired
    UserSession userSession;

    @Override
    public void authorizationCode(String responseType, String clientId, String redirectUri, String scope, String state, HttpServletRequest req, HttpServletResponse resp) {
        // 检测用户已经登录，如果没跳到登录页面让用户输入帐密
        // 如果已经登录，则提示转到一个页面，询问用户是否同意，授权可访问
        // 若是则生成 code，跳转到 redirectUri，那是一个回调
        sendAuthCode(userSession, responseType, clientId, redirectUri, scope, state, null, req, resp, cache);
    }

    /**
     * @param webUrl 前端页面地址，用于跳到这里以便获取 Token
     */
    public static void sendAuthCode(UserSession userSession, String responseType, String clientId, String redirectUri, String scope, String state, String webUrl, HttpServletRequest req, HttpServletResponse resp, Cache<String, Object> cache) {
        if (!"code".equals(responseType))
            throw new IllegalArgumentException("参数 response_type 只能是 code");

        User user = userSession.getUserFromSession();

        if (user == null) { // 未登录
            // 返回一段 HTML
            String qs = req.getQueryString();
            String loginPage;

            // 根据 appId 获取登录地址
//            loginPage = Sql.newInstance().input("SELECT login_page FROM app WHERE stat = 0 AND client_id = ?", clientId).queryOne(String.class);
            // 根据 租户 获取登录地址
            if (TenantService.getTenantId() == null) // 没租户 id，超级管理员登录
                loginPage = "../../iam/login";
            else
                loginPage = new Action("SELECT login_page FROM tenant WHERE stat = 0 AND id = ?").query(TenantService.getTenantId()).one(String.class);

            if (ObjectHelper.isEmptyText(loginPage))
                throw new BusinessException("应用或登录地址不存在");

            String html = String.format(NOT_LOGIN_TEXT, loginPage + "?" + qs);
            UserUtils.responseHTML(resp, html);
        } else {// 已登录，发送授权码
            StringBuilder sb = new StringBuilder();
            sb.append("?state=").append(state);
            // 生成授权码（Authorization Code）
            String code = HashHelper.getSHA1(clientId + RandomTools.generateRandomString(6));
            sb.append("&code=").append(code);

            if (StringUtils.hasText(webUrl))
                sb.append("&web_url=").append(webUrl);

            if (!StringUtils.hasText(scope))
                scope = DEFAULT_SCOPE;

            if (user.getTenantId() != null)
                scope += ";tenantId=" + user.getTenantId();

            cache.put(code + ":user", user, AUTHORIZATION_CODE_TIMEOUT); // 保存本次请求所属的用户信息
            cache.put(code + ":scope", scope, AUTHORIZATION_CODE_TIMEOUT);// 保存本次请求的授权范围

            UserUtils.send303Redirect(resp, redirectUri + sb); // 跳转到客户机
        }
    }

    @Data
    static class TokenUser {
        Long userId;

        AccessToken accessToken;
    }

    /**
     * Token 的有效期，单位：分钟  默认一天
     */
    @Value("${oauth.token.client_expires: 3600}")
    private Integer tokenExpires;

    @Override
    public AccessToken token(String authorization, String grantType, String code, String state) {
        if (!"authorization_code".equals(grantType))
            throw new IllegalArgumentException("参数 grant_type 只能是 authorization_code");

        String scope = cache.get(code + ":scope", String.class);
        User user = cache.get(code + ":user", User.class);

        // 如果能够通过 Authorization Code 获取到对应的用户信息，则说明该 Authorization Code 有效
        if (user == null)
            throw new IllegalArgumentException("非法 code：" + code);

        App app = ClientCredential.getAppByAuthHeader(authorization);

        ClassicAccessToken tokenService = new ClassicAccessToken(app, GrantType.OAUTH, user);
        tokenService.setTokenExpires(tokenExpires);
        AccessToken accessToken = tokenService.create();
        tokenService.createSave(accessToken);

        // 保存 token 在缓存
        TokenUser tokenUser = new TokenUser();
        String key = TOKEN_USER_KEY + "-" + accessToken.getAccess_token();
        cache.put(key, tokenUser, accessToken.getExpires_in() * 1000);

        // 删除缓存
        cache.remove(code + ":scope");
        cache.remove(code + ":user");

        return accessToken;
    }

    @Override
    @EnableTransaction
    public AccessToken refreshToken(String grantType, String refreshToken) {
        if (!GrantType.REFRESH_TOKEN.equals(grantType))
            throw new IllegalArgumentException("grantType must be 'refresh_token'");

        String appId = ClientCredential.getAppId();
        App app = ClientCredential.getApp(appId);
        ClassicAccessToken tokenService = new ClassicAccessToken(app, GrantType.OAUTH);
        tokenService.setTokenExpires(tokenExpires);

        return tokenService.refreshToken(refreshToken);
    }

    @Override
    public Boolean checkToken(String token) {
        return null;
    }

    @Override
    public Boolean revokeToken(String token) {
        return false;
    }
}