package com.ajaxjs.iam.server.auth;

import com.ajaxjs.iam.jwt.JwtAccessToken;
import com.ajaxjs.iam.model.App;
import com.ajaxjs.iam.server.auth.controller.UserLoginController;
import com.ajaxjs.iam.server.model.User;
import com.ajaxjs.iam.server.service.ClientCredential;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserLoginService extends OAuthCommon implements UserLoginController {
    @Override
    public JwtAccessToken login(String username, String password, String appId) {
        return null;
    }

    @Override
    public JwtAccessToken loginByClient(String username, String password) {
        return null;
    }

    @Override
    public JwtAccessToken ropcToken(String grantType, String username, String password, String clientId, String clientSecret, String scope) {
        if (!"password".equals(grantType))
            throw new IllegalArgumentException("参数 grant_type 只能是 password");

        App app = ClientCredential.getApp(clientId, clientSecret);
        Integer tenantId = app.getTenantId();
        User user = userLoginRegisterService.getUserLoginByPassword(username, password, tenantId);

        return createJWTByUser(user, app, scope);
    }
}
