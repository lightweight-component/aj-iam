package com.ajaxjs.iam.server.service.token;

import com.ajaxjs.framework.model.BusinessException;
import com.ajaxjs.iam.jwt.JWebToken;
import com.ajaxjs.iam.jwt.JWebTokenMgr;
import com.ajaxjs.iam.jwt.JwtUtils;
import com.ajaxjs.iam.model.App;
import com.ajaxjs.iam.server.common.IamConstants;
import com.ajaxjs.iam.server.model.User;
import com.ajaxjs.iam.server.service.TenantService;
import com.ajaxjs.iam.server.service.token.model.AccessTokenPo;
import com.ajaxjs.iam.jwt.JwtToken;
import com.ajaxjs.iam.server.user_info.UserInfoService;
import com.ajaxjs.spring.DiContextUtil;
import com.ajaxjs.sqlman.Action;
import com.ajaxjs.util.ObjectHelper;
import com.ajaxjs.util.RandomTools;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JwtTokenService extends BaseTokenService {
    public JwtTokenService(App app, User user) {
        super(app, IamConstants.GrantType.OIDC, user);
    }

    public JwtTokenService(App app, String grantType) {
        super(app, grantType);
    }

    public JwtTokenService(App app) {
        this(app, IamConstants.GrantType.OIDC);
    }

    static final String DEFAULT_SCOPE = "";

    private JWebTokenMgr jWebTokenMgr;

    public void setjWebTokenMgr(JWebTokenMgr jWebTokenMgr) {
        this.jWebTokenMgr = jWebTokenMgr;
    }

    public JWebTokenMgr getjWebTokenMgr() {
        if (jWebTokenMgr == null)
            jWebTokenMgr = DiContextUtil.getBean(JWebTokenMgr.class);

        if (jWebTokenMgr == null)
            throw new NullPointerException("Please set jWebTokenMgr first.");

        return jWebTokenMgr;
    }

    /**
     * 生成 JWT Token
     *
     * @return JWT Token
     */
    public JwtToken create() {
        App app = getApp();
        User user = getUser();

        Integer tokenExpires = getTokenExpires();
        Integer refreshTokenExpires = getRefreshTokenExpires();
        int tokenExpInSecs = app.getExpires() == null ? tokenExpires * 60 : app.getExpires() * 60;
        int refreshTokenExpInSecs = app.getRefreshExpires() == null ? refreshTokenExpires * 60 : app.getRefreshExpires() * 60;

        JwtToken jwtAccessToken = new JwtToken();
        jwtAccessToken.setExpiresIn(tokenExpInSecs);
        jwtAccessToken.setRefreshExpiresIn(refreshTokenExpInSecs);


        // TODO user.getName() 中文名会乱码
        Long[][] userPermissions = getUserPermissions(user.getId());
        JWebToken jWebToken = getjWebTokenMgr().tokenFactory(
                String.valueOf(user.getId()), user.getLoginId(), DEFAULT_SCOPE, JwtUtils.setExpire(tokenExpInSecs / 3600),
                user.getTenantId().intValue(), userPermissions[0], userPermissions[1]
        );
        jwtAccessToken.setToken(jWebToken.toString());
        jwtAccessToken.setTokenJson(jWebToken.getPayloadJson());
        jwtAccessToken.setRefreshToken(RandomTools.uuidStr());

        return jwtAccessToken;
    }

    public boolean createSave(JwtToken token) {
        // 保存 token
        AccessTokenPo save = new AccessTokenPo();
        save.setAccessToken(token.getToken());
        save.setRefreshToken(token.getRefreshToken());
        save.setJwtToken(token.getTokenJson());
        save.setExpiresDate(calculateExpirationDate(token.getExpiresIn()));
        save.setRefreshExpires(calculateExpirationDate(token.getRefreshExpiresIn()));
        save.setGrantType(getGrantType());
        save.setClientId(getApp().getClientId());
        save.setCreateDate(new Date());

        token.setTokenJson(null); // 避免把 json 输出到前端

        if (TenantService.getTenantId(false) != null)
            save.setTenantId(TenantService.getTenantId(false));

        User user = getUser();

        if (user != null) {
            save.setUserId(user.getId());
            save.setUserName(user.getLoginId());
        }

        return new Action(save).create().execute(true).isOk();
    }

    public JwtToken refreshToken(String refreshToken) {
        AccessTokenPo accessTokenPO = new Action("SELECT * FROM access_token WHERE refresh_token = ?").query(refreshToken).one(AccessTokenPo.class);

        if (accessTokenPO == null)
            throw new BusinessException("找不到 RefreshToken " + refreshToken);

        User user = UserInfoService.getUserByIdSimple(accessTokenPO.getUserId());
        setUser(user);

        // TODO checks refresh token if it's expired
        JwtToken token = create();

        // 修改旧的
        AccessTokenPo updated = new AccessTokenPo();
        updated.setId(accessTokenPO.getId());
        updated.setAccessToken(token.getToken());
        updated.setRefreshToken(token.getRefreshToken());
        updated.setExpiresDate(calculateExpirationDate(token.getExpiresIn()));
        updated.setRefreshExpires(calculateExpirationDate(token.getRefreshExpiresIn()));

        new Action(updated).update().withId();

        return token;
    }

    /**
     * 获取用户权限
     *
     * @param userId 用户 ID
     * @return 用户权限
     */
    public static Long[][] getUserPermissions(Long userId) {
        String sql = "SELECT module_value, permission_value FROM per_role WHERE id IN (SELECT role_id FROM per_user_role WHERE user_id = ?)";
        List<Map<String, Object>> result = new Action(sql).query(userId).list();
        List<Long> permissions = new ArrayList<>();
        List<Long> modulePermissions = new ArrayList<>();

        if (!ObjectHelper.isEmpty(result)) {
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
