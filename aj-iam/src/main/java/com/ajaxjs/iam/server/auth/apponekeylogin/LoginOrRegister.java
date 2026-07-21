package com.ajaxjs.iam.server.auth.apponekeylogin;

import com.ajaxjs.iam.jwt.JWebTokenMgr;
import com.ajaxjs.iam.jwt.JwtAccessToken;
import com.ajaxjs.iam.jwt.JwtUtils;
import com.ajaxjs.iam.model.App;
import com.ajaxjs.iam.oauth.ClientCredential;
import com.ajaxjs.iam.server.common.IamConstants;
import com.ajaxjs.iam.server.model.User;
import com.ajaxjs.iam.server.model.UserAccount;
import com.ajaxjs.iam.server.model.UserAccountType;
import com.ajaxjs.iam.server.auth.OAuthCommon;
import com.ajaxjs.iam.server.auth.OidcService;
import com.ajaxjs.iam.server.service.TenantService;
import com.ajaxjs.iam.server.model.UserFunction;
import com.ajaxjs.sqlman.Action;
import com.ajaxjs.util.ObjectHelper;
import com.ajaxjs.util.RandomTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginOrRegister extends OAuthCommon {
    @Autowired
    AliyunOpenApi aliyunOpenApi;

    public JwtAccessToken byPhone(String token) {
        String phone = aliyunOpenApi.getPhoneByToken(token);
        log.info("已获取手机号码 {}", phone);

        return createUserByPhone(phone);
    }

    public JwtAccessToken createUserByPhone(String phone) {
        Integer tenantId = TenantService.getTenantId(false);

        if (tenantId == null)
            throw new IllegalArgumentException("请选择租户");

        User user;
        User existUser = new Action("SELECT * FROM user WHERE stat = 0 AND phone = ? AND tenant_id = ?").query(phone, tenantId).one(User.class);
        boolean isNewlyUser = existUser == null;

        if (isNewlyUser) { // to create a new user
            user = createUser(tenantId.longValue(), phone);
            createUserAccount(user.getId());
        } else {
            user = existUser;
            UserAccount existAccount = new Action("SELECT * FROM user_account WHERE user_id = ? AND type = 'PHONE' AND stat= 0")
                    .query(existUser.getId()).one(UserAccount.class);

            if (existAccount == null)
                createUserAccount(existUser.getId());
        }

        // TODO 暂时写死 app id
        return createToken(user, ClientCredential.getApp("r3fgO43ft5H"), isNewlyUser);
    }

    private static User createUser(Long tenantId, String phoneNumber) {
        User user = new User();
        user.setLoginId("User_" + RandomTools.generateRandomString(5));
        user.setTenantId(tenantId);
        user.setBindState(UserFunction.BindState.APP);

        if (ObjectHelper.hasText(phoneNumber))
            user.setPhone(phoneNumber);

        Long newlyId = new Action(user).create().execute(true, Long.class).getNewlyId();
        user.setId(newlyId);

        return user;
    }

    private static boolean createUserAccount(Long userId) {
        UserAccount account = new UserAccount();
        account.setUserId(userId);
        account.setType(UserAccountType.PHONE);

        return new Action(account).create().execute(true).isOk();
    }

    @Autowired
    JWebTokenMgr jWebTokenMgr;

    @Value("${User.oidc.jwtExpireHours:74}")
    int jwtExpireHours;

    JwtAccessToken createToken(User user, App app, Boolean isNewlyUser) {
        // 生成 JWT Token
        JwtAccessToken accessToken = new JwtAccessToken();
        accessToken.setIsNewlyUser(isNewlyUser);

        // TODO user.getName() 中文名会乱码
        Long[][] userPermissions = OidcService.getUserPermissions(user.getId());
        String jWebToken = jWebTokenMgr.tokenFactory(
                String.valueOf(user.getId()), user.getLoginId(), "", JwtUtils.setExpire(jwtExpireHours),
                user.getTenantId().intValue(), userPermissions[0], userPermissions[1]
        ).toString();
        accessToken.setId_token(jWebToken);
        createToken(accessToken, app, IamConstants.GrantType.OIDC, user);

        return accessToken;
    }
}
