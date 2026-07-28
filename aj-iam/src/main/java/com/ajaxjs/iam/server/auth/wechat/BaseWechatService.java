package com.ajaxjs.iam.server.auth.wechat;

import com.ajaxjs.iam.jwt.JWebTokenMgr;
import com.ajaxjs.iam.model.App;
import com.ajaxjs.iam.server.model.User;
import com.ajaxjs.iam.server.model.UserAccount;
import com.ajaxjs.iam.server.model.UserAccountType;
import com.ajaxjs.iam.server.model.UserFunction;
import com.ajaxjs.iam.server.model.wechat.Code2SessionResult;
import com.ajaxjs.iam.server.service.token.JwtTokenService;
import com.ajaxjs.iam.server.service.token.model.JwtToken;
import com.ajaxjs.iam.server.user_info.UserInfoService;
import com.ajaxjs.sqlman.Action;
import com.ajaxjs.util.ObjectHelper;
import com.ajaxjs.util.RandomTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public abstract class BaseWechatService {
    @Autowired
    JWebTokenMgr jWebTokenMgr;

    JwtToken createOrUpdateUser(String openId, String sessionKey, App app, long tenantId) {
        UserAccount account = new Action("SELECT * FROM user_account WHERE stat != 1 AND identifier = ? AND type = 'WECHAT_MINI'").query(openId).one(UserAccount.class);
        boolean isNewlyUser = account == null;
        User user;

        if (!isNewlyUser) { // exists account
            Long userId = account.getUserId();
            user = UserInfoService.getUserByIdSimple(userId);

            if (sessionKey != null) {
                // saves a session key
                UserAccount saveSessionKey = new UserAccount();
                saveSessionKey.setId(account.getId());
                saveSessionKey.setIdentifier2(sessionKey);

                new Action(saveSessionKey).update().withId();
            }
        } else { // to create a new account
            user = createUser(tenantId, null);
            createUserAccount(user.getId(), openId, sessionKey);
        }

        return createToken(user, app, isNewlyUser);
    }

    /**
     * Token 的有效期，单位：分钟  默认一天
     */
    @Value("${oauth.token.client_expires: 3600}")
    private Integer tokenExpires;

    JwtToken createToken(User user, App app, Boolean isNewlyUser) {
        JwtTokenService tokenService = new JwtTokenService(app, user);
        tokenService.setjWebTokenMgr(jWebTokenMgr);
        tokenService.setTokenExpires(tokenExpires);
        JwtToken token = tokenService.create();
        token.setIsNewlyUser(isNewlyUser);
        tokenService.createSave(token);

        return token;
    }

    static User createUser(Long tenantId, String phoneNumber) {
        User user = new User();
        user.setLoginId("WxMiniUser_" + RandomTools.generateRandomString(5));
        user.setTenantId(tenantId);
        user.setBindState(UserFunction.BindState.WECHAT);

        if (ObjectHelper.hasText(phoneNumber))
            user.setPhone(phoneNumber);

        Long newlyId = new Action(user).create().execute(true, Long.class).getNewlyId();
        user.setId(newlyId);

        return user;
    }

    public boolean createUserAccount(Long userId, Code2SessionResult session) {
        return createUserAccount(userId, session.getOpenid(), session.getSession_key());
    }

    private static boolean createUserAccount(Long userId, String openId, String sessionKey) {
        UserAccount account = new UserAccount();
        account.setUserId(userId);
        account.setIdentifier(openId);

        if (sessionKey != null)
            account.setIdentifier2(sessionKey);

        account.setType(UserAccountType.WECHAT_MINI);

        return new Action(account).create().execute(true).isOk();
    }
}
