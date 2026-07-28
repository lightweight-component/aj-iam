package com.ajaxjs.iam.server.authorizatio;

import com.ajaxjs.iam.model.AccessToken;
import com.ajaxjs.iam.model.App;
import com.ajaxjs.iam.server.authorizatio.controller.ClientCredentialController;
import com.ajaxjs.iam.server.common.IamConstants;
import com.ajaxjs.iam.server.service.ClientCredential;
import com.ajaxjs.iam.server.service.token.ClassicAccessToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ClientCredentialService implements ClientCredentialController {
    @Override
    public boolean register(App app) {
        return new ClientCredential().clientRegister(app);
    }

    @Override
    public AccessToken clientCredential(String grantType) {
        if (!IamConstants.GrantType.CLIENT_CREDENTIALS.equals(grantType))
            throw new IllegalArgumentException("grantType must be 'clientCredentials'");

        String appId = ClientCredential.getAppId();
        App app = ClientCredential.getApp(appId);
        ClassicAccessToken tokenService = new ClassicAccessToken(app, IamConstants.GrantType.CLIENT_CREDENTIALS);
        AccessToken accessToken = tokenService.create();
        tokenService.createSave(accessToken);

        return accessToken;
    }

//    @Override
//    public JwtAccessToken clientCredentials(String grantType, String authorization) {
//        if (!IamConstants.GrantType.CLIENT_CREDENTIALS.equals(grantType))
//            throw new IllegalArgumentException("grantType must be 'clientCredentials'");
//
//        App app = ClientCredential.getAppByAuthHeader(authorization);
//
//        // 生成 Access Token
//        JwtAccessToken accessToken = new JwtAccessToken();
//        createToken(accessToken, app, IamConstants.GrantType.OIDC);
//
//        // 生成 JWT Token
//        // 目前 client 没有权限机制
//        String jWebToken = jWebTokenMgr.tokenFactory(String.valueOf(app.getId()), app.getName(), "", 0L /* 0 表示不过期*/, null, null, null).toString();
//        accessToken.setId_token(jWebToken);
//
//        return accessToken;
//    }
}
