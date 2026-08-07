package com.ajaxjs.iam.server.auth.apponekeylogin;

import com.ajaxjs.iam.jwt.JwtToken;
import com.ajaxjs.iam.server.model.User;
import com.ajaxjs.iam.server.model.UserAccount;
import com.ajaxjs.iam.server.model.UserAccountType;
import com.ajaxjs.iam.server.model.UserFunction;
import com.ajaxjs.iam.server.service.ClientCredential;
import com.ajaxjs.iam.server.service.TenantService;
import com.ajaxjs.iam.server.service.token.JwtTokenService;
import com.ajaxjs.sqlman.Action;
import com.ajaxjs.util.ObjectHelper;
import com.ajaxjs.util.RandomTools;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginOrRegister {
    private final UserAccountType userAccountType;

    public JwtToken createUser(String value, String appId) {
        Integer tenantId = TenantService.getTenantId(false);

        if (tenantId == null)
            throw new IllegalArgumentException("请选择租户");

        String type = switch (userAccountType) {
            case PHONE -> "phone";
            case EMAIL -> "email";
            default -> "";
        };

        User user;
        User existUser = new Action("SELECT * FROM user WHERE stat = 0 AND " + type + " = ? AND tenant_id = ?").query(value, tenantId).one(User.class);
        boolean isNewlyUser = existUser == null;

        if (isNewlyUser) { // to create a new user
            user = createUser(tenantId.longValue(), value);
            createUserAccount(user.getId());
        } else {
            user = existUser;
            UserAccount existAccount = new Action("SELECT * FROM user_account WHERE user_id = ? AND stat= 0")
                    .query(existUser.getId()).one(UserAccount.class);

            if (existAccount == null)
                createUserAccount(existUser.getId());
        }

        JwtTokenService tokenService = new JwtTokenService(ClientCredential.getApp(appId), user);
        JwtToken token = tokenService.create();
        token.setIsNewlyUser(isNewlyUser);
        tokenService.createSave(token);

        return token;
    }

    private User createUser(Long tenantId, String value) {
        User user = new User();
        user.setLoginId("User_" + RandomTools.generateRandomString(5));
        user.setTenantId(tenantId);
        user.setBindState(UserFunction.BindState.APP);

        if (ObjectHelper.hasText(value)) {
            switch (userAccountType) {
                case PHONE:
                    user.setPhone(value);
                    break;
                case EMAIL:
                    user.setEmail(value);
                    break;
            }
        }

        Long newlyId = new Action(user).create().execute(true, Long.class).getNewlyId();
        user.setId(newlyId);

        return user;
    }

    private boolean createUserAccount(Long userId) {
        UserAccount account = new UserAccount();
        account.setUserId(userId);
        account.setType(userAccountType);

        return new Action(account).create().execute(true).isOk();
    }
}
