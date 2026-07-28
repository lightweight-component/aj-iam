package com.ajaxjs.iam.server.authorizatio.controller;

import com.ajaxjs.iam.annotation.ClientAuthentication;
import com.ajaxjs.iam.model.AccessToken;
import com.ajaxjs.iam.model.App;
import com.ajaxjs.spring.annotation.BizAction;
import org.springframework.web.bind.annotation.*;

/**
 * 客户端认证
 */
@RestController
@RequestMapping("/oauth/client")
public interface ClientCredentialController {
    @PostMapping
    @BizAction("客户端认证注册")
    @ClientAuthentication
    boolean register(@RequestBody App app);

    /**
     * 客户端凭证获取 Token
     * 这是通过头传输 client_id/client_secret 用 Base64 编码
     *
     * @param grantType 必填，且固定是 client_credentials
     * @return 客户端的 AccessToken
     */
    @PostMapping("/token")
    @ClientAuthentication
    @BizAction("客户端凭证获取 Token")
    AccessToken clientCredential(@RequestParam("grant_type") String grantType);
}
