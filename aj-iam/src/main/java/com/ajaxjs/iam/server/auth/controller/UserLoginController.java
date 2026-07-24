package com.ajaxjs.iam.server.auth.controller;

import com.ajaxjs.iam.annotation.AllowOpenAccess;
import com.ajaxjs.iam.annotation.ClientAuthentication;
import com.ajaxjs.iam.jwt.JwtAccessToken;
import com.ajaxjs.security.captcha.image.ImageCaptchaCheck;
import com.ajaxjs.spring.annotation.BizAction;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户登录
 */
@RestController
@RequestMapping("/user_login")
public interface UserLoginController {
    /**
     * 用户登录
     *
     * @param username 用户名/手机号/邮箱
     * @param password 密码
     * @param appId    应用 id
     * @return 应用的 JWT AccessToken
     */
    @PostMapping("/login")
    @AllowOpenAccess
    @ImageCaptchaCheck
    @BizAction("用户登录")
    JwtAccessToken login(@RequestParam String username, @RequestParam String password, @RequestParam String appId);

    /**
     * 用户登录（配合客户端认证方式）
     *
     * @param username 用户名/手机号/邮箱
     * @param password 密码
     * @return 应用的 JWT AccessToken
     */
    @PostMapping("/login_by_client")
    @ClientAuthentication
    @BizAction("用户登录（配合客户端认证方式）")
    JwtAccessToken loginByClient(@RequestParam String username, @RequestParam String password);

    /**
     * Resource Owner Password Credentials(ROPC) 密码模式获取 Token。
     * 客户端可能不可信，存在密码泄露风险，一般情况下尽量避免使用。
     *
     * @param grantType    必填，且固定是 password
     * @param username     用户账号
     * @param password     密码
     * @param clientId     客户机应用 id
     * @param clientSecret 应用客户端密钥
     * @param scope        权限范围
     * @return 应用的 JWT AccessToken
     */
    @PostMapping("/ropc_token")
    @Deprecated
    JwtAccessToken ropcToken(@RequestParam("grant_type") String grantType,
                             @RequestParam String username, @RequestParam String password,
                             @RequestParam("client_id") String clientId,
                             @RequestParam("client_secret") String clientSecret,
                             @RequestParam(required = false) String scope);
}
