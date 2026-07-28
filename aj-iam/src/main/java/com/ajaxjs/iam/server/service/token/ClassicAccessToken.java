package com.ajaxjs.iam.server.service.token;

import com.ajaxjs.framework.model.BusinessException;
import com.ajaxjs.iam.model.AccessToken;
import com.ajaxjs.iam.model.App;
import com.ajaxjs.iam.server.model.User;
import com.ajaxjs.iam.server.service.TenantService;
import com.ajaxjs.iam.server.service.token.model.AccessTokenPo;
import com.ajaxjs.sqlman.Action;
import com.ajaxjs.util.RandomTools;

import java.util.Date;

/**
 * 传统的普通 Access Token
 */

public class ClassicAccessToken extends BaseTokenService {
    public ClassicAccessToken(App app, String grantType, User user) {
        super(app, grantType, user);
    }

    public ClassicAccessToken(App app, String grantType) {
        super(app, grantType);
    }

    /**
     * 生成 Access Token
     *
     * @return 生成的 Access Token
     */
    public AccessToken create() {
        App app = getApp();
        Integer tokenExpires = getTokenExpires();
        Integer refreshTokenExpires = getRefreshTokenExpires();

        AccessToken accessToken = new AccessToken();
        accessToken.setAccess_token(RandomTools.uuidStr());
        accessToken.setRefresh_token(RandomTools.uuidStr());

        int tokenExpInSecs = app.getExpires() == null ? tokenExpires * 60 : app.getExpires() * 60;
        int refreshTokenExpInSecs = app.getRefreshExpires() == null ? refreshTokenExpires * 60 : app.getRefreshExpires() * 60;
        accessToken.setExpires_in(tokenExpInSecs);
        accessToken.setRefresh_expires_in(refreshTokenExpInSecs);

        return accessToken;
    }

    public boolean createSave(AccessToken accessToken) {
        // 保存 token
        AccessTokenPo save = new AccessTokenPo();
        save.setAccessToken(accessToken.getAccess_token());
        save.setRefreshToken(accessToken.getRefresh_token());
        save.setExpiresDate(calculateExpirationDate(accessToken.getExpires_in()));
        save.setRefreshExpires(calculateExpirationDate(accessToken.getRefresh_expires_in()));
        save.setGrantType(getGrantType());
        save.setClientId(getApp().getClientId());
        save.setCreateDate(new Date());

        if (TenantService.getTenantId(false) != null)
            save.setTenantId(TenantService.getTenantId(false));

        User user = getUser();

        if (user != null) {
            save.setUserId(user.getId());
            save.setUserName(user.getLoginId());
        }

//        return new Action(save).create().execute(true).isOk();
        return true;
    }

    public AccessToken refreshToken(String refreshToken) {
        AccessTokenPo accessTokenPO = new Action("SELECT * FROM access_token WHERE refresh_token = ?").query(refreshToken).one(AccessTokenPo.class);

        if (accessTokenPO == null)
            throw new BusinessException("找不到 RefreshToken " + refreshToken);

        // TODO checks refresh token if it's expired
        AccessToken accessToken = create();

        // 修改旧的
        AccessTokenPo updated = new AccessTokenPo();
        updated.setId(accessTokenPO.getId());
        updated.setAccessToken(accessToken.getAccess_token());
        updated.setRefreshToken(accessToken.getRefresh_token());
        updated.setExpiresDate(calculateExpirationDate(accessToken.getExpires_in()));
        updated.setRefreshExpires(calculateExpirationDate(accessToken.getRefresh_expires_in()));

//        new Action(updated).update().withId();

        return accessToken;
    }

    public boolean checkToken() {
        return false;
    }


    public boolean revokeToken() {
        return false;
    }
}
