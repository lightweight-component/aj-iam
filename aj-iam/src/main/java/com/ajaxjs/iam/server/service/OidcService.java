package com.ajaxjs.iam.server.service;

import com.ajaxjs.framework.cache.Cache;
import com.ajaxjs.framework.database.EnableTransaction;
import com.ajaxjs.framework.model.BusinessException;
import com.ajaxjs.iam.client.BaseOidcClientUserController;
import com.ajaxjs.iam.jwt.JWebTokenMgr;
import com.ajaxjs.iam.jwt.JwtUtils;
import com.ajaxjs.iam.server.controller.OidcController;
import com.ajaxjs.iam.jwt.JwtAccessToken;
import com.ajaxjs.iam.server.model.po.AccessTokenPo;
import com.ajaxjs.iam.server.model.po.App;
import com.ajaxjs.iam.server.common.session.UserSession;
import com.ajaxjs.iam.server.model.User;
import com.ajaxjs.spring.DiContextUtil;
import com.ajaxjs.sqlman.Action;
import com.ajaxjs.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OidcService extends OAuthCommon implements OidcController {
    @Autowired
    UserSession userSession;

    @Autowired(required = false)
    Cache<String, Object> cache;

    public Cache<String, Object> getCache() {
        return cache;
    }

    @Autowired
    JWebTokenMgr jWebTokenMgr;

    @Value("${User.oidc.jwtExpireHours:74}")
    int jwtExpireHours;

    @Override
    public void authorization(String responseType, String clientId, String redirectUri, String scope, String state, String webUrl, HttpServletRequest req, HttpServletResponse resp) {
        sendAuthCode(responseType, clientId, redirectUri, scope, state, webUrl, req, resp, cache);
    }

    @Data
    public static class TokenUser {
        Long userId;

        JwtAccessToken accessToken;
    }

    @Override
    public JwtAccessToken token(String authorization, String grantType, String code, String state, String webUrl) {
        if (!"authorization_code".equals(grantType))
            throw new IllegalArgumentException("参数 grant_type 只能是 authorization_code");

        User user = cache.get(code + ":user", User.class);
        String scope = cache.get(code + ":scope", String.class);

        // 如果能够通过 Authorization Code 获取到对应的用户信息，则说明该 Authorization Code 有效
        if (StringUtils.hasText(scope) && user != null) {
            App app = getAppByAuthHeader(authorization);

            // 生成 Access Token
            JwtAccessToken accessToken = new JwtAccessToken();
            // 生成 JWT Token
            // TODO user.getName() 中文名会乱码
            Long[][] userPermissions = getUserPermissions(user.getId());
            String jWebToken = jWebTokenMgr.tokenFactory(
                    String.valueOf(user.getId()), user.getLoginId(), scope, getJwtExpireTimeStamp(app),
                    user.getTenantId().intValue(), userPermissions[0], userPermissions[1]
            ).toString();
            accessToken.setId_token(jWebToken);

            createToken(accessToken, app, GrantType.OIDC, user);

            // 保存 token 在缓存
            saveTokenToCache(user, accessToken, app);

            // 删除缓存
            cache.remove(code + ":scope");
            cache.remove(code + ":user");

            return accessToken;
        } else
            throw new IllegalArgumentException("非法 code：" + code);
    }

    @Override
    @EnableTransaction
    public JwtAccessToken refreshToken(String grantType, String authorization, String refreshToken) {
        if (!GrantType.REFRESH_TOKEN.equals(grantType))
            throw new IllegalArgumentException("GrantType must be 'refresh_token'.");

        App app = getAppByAuthHeader(authorization);
        AccessTokenPo accessTokenPO = new Action("SELECT * FROM access_token WHERE refresh_token = ?").query(refreshToken).one(AccessTokenPo.class);

        if (accessTokenPO == null)
            throw new BusinessException("找不到 RefreshToken " + refreshToken);

        User user = UserService.getUserById(accessTokenPO.getUserId());

        // 生成 Access Token
        JwtAccessToken accessToken = new JwtAccessToken();
        // 生成 JWT Token
        // TODO user.getName() 中文名会乱码
        Long[][] userPermissions = getUserPermissions(user.getId());
        String jWebToken = jWebTokenMgr.tokenFactory(
                String.valueOf(user.getId()), user.getLoginId(), DEFAULT_SCOPE, getJwtExpireTimeStamp(app),
                user.getTenantId().intValue(), userPermissions[0], userPermissions[1]
        ).toString();
        accessToken.setId_token(jWebToken);

        Date[] arr = createToken(accessToken, app);
        saveTokenToCache(user, accessToken, app); // 保存 token 在缓存

        // 修改旧的
        AccessTokenPo updated = new AccessTokenPo();
        updated.setId(accessTokenPO.getId());
        updated.setAccessToken(accessToken.getAccess_token());
        updated.setRefreshToken(accessToken.getRefresh_token());
        updated.setExpiresDate(arr[0]);
        updated.setRefreshExpires(arr[1]);
        updated.setJwtToken(JsonUtil.toJson(accessToken));

        new Action(updated).update().withId();

        return accessToken;
    }

    /**
     * 保存 token 在缓存
     */
    void saveTokenToCache(User user, JwtAccessToken accessToken, App app) {
        TokenUser tokenUser = new TokenUser();
        tokenUser.setUserId(user.getId());
        tokenUser.setAccessToken(accessToken);

        String key = JWT_TOKEN_USER_KEY + "-" + accessToken.getId_token();
        cache.put(key, tokenUser, getTokenExpires(app));
        log.info("save user {} to cache, key: {}", tokenUser, key);
    }

    @Autowired
    UserRegisterService userLoginRegisterService;

    @Autowired
    LogLoginService logLoginService;

    @Override
    public JwtAccessToken ropcToken(String grant_type, String username, String password, String client_id, String client_secret, String scope) {
        if (!"password".equals(grant_type))
            throw new IllegalArgumentException("参数 grant_type 只能是 password");

        App app = new Action("SELECT * FROM app WHERE stat != 1 AND client_id = ? AND client_secret = ?").query(client_id, client_secret).one(App.class);

        if (app == null)
            throw new UnsupportedOperationException("App Not found: " + client_id);

        Integer tenantId = app.getTenantId();
        User user = userLoginRegisterService.getUserLoginByPassword(username, password, tenantId);

        // 生成 Access Token
        JwtAccessToken accessToken = new JwtAccessToken();

        // 生成 JWT Token
        // TODO user.getName() 中文名会乱码
        Long[][] userPermissions = getUserPermissions(user.getId());
        String jWebToken = jWebTokenMgr.tokenFactory(
                String.valueOf(user.getId()), user.getLoginId(), scope, JwtUtils.setExpire(jwtExpireHours),
                user.getTenantId().intValue(), userPermissions[0], userPermissions[1]
        ).toString();
        accessToken.setId_token(jWebToken);
        createToken(accessToken, app, GrantType.OIDC, user);

        // 保存 token 在缓存
        TokenUser tokenUser = new TokenUser();
        tokenUser.setUserId(user.getId());
        tokenUser.setAccessToken(accessToken);

        String key = JWT_TOKEN_USER_KEY + "-" + jWebToken;
        cache.put(key, tokenUser, getTokenExpires(app));
        log.info("save user {} to cache, key: {}", tokenUser, key);

        BaseOidcClientUserController.setTokenToCookie(accessToken, DiContextUtil.getResponse());
        logLoginService.saveLoginLog(user, DiContextUtil.getRequest());

        return accessToken;
    }

    @Override
    public JwtAccessToken clientCredentials(String grantType, String authorization) {
        if (!GrantType.CLIENT_CREDENTIALS.equals(grantType))
            throw new IllegalArgumentException("grantType must be 'clientCredentials'");

        App app = getAppByAuthHeader(authorization);

        // 生成 Access Token
        JwtAccessToken accessToken = new JwtAccessToken();
        createToken(accessToken, app, GrantType.OIDC);

        // 生成 JWT Token
        // 目前 client 没有权限机制
        String jWebToken = jWebTokenMgr.tokenFactory(String.valueOf(app.getId()), app.getName(), "", 0L /* 0 表示不过期*/, null, null, null).toString();
        accessToken.setId_token(jWebToken);

        return accessToken;
    }

    public static Long[][]

    getUserPermissions(Long userId) {
        String sql = "SELECT module_value, permission_value FROM per_role WHERE id IN (SELECT role_id FROM per_user_role WHERE user_id = ?)";
        List<Map<String, Object>> result = new Action(sql).query(userId).list();

        List<Long> permissions = new ArrayList<>();
        List<Long> modulePermissions = new ArrayList<>();

        if (result != null && !result.isEmpty()) {
            result.forEach(item -> {
                Object _permissionValue = item.get("permissionValue");

                if (_permissionValue != null) {
                    Long permissionValue = (Long) _permissionValue;

                    if (permissionValue != 0L)
                        permissions.add(permissionValue);
                }

                Object _moduleValue = item.get("moduleValue");

                if (_moduleValue != null) {
                    Long moduleValue = (Long) _moduleValue;

                    if (moduleValue != 0L)
                        modulePermissions.add(moduleValue);
                }
            });
        }

        Long[][] _permissions = new Long[2][];

        _permissions[0] = permissions.isEmpty() ? null : permissions.toArray(new Long[0]);
        _permissions[1] = modulePermissions.isEmpty() ? null : modulePermissions.toArray(new Long[0]);

        return _permissions;
    }
}
