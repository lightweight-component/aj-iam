package com.ajaxjs.iam.server.auth.controller;

import com.ajaxjs.iam.annotation.AllowOpenAccess;
import com.ajaxjs.iam.annotation.ClientAuthentication;
import com.ajaxjs.iam.jwt.JwtToken;
import com.ajaxjs.security.captcha.image.ImageCaptchaCheck;
import com.ajaxjs.spring.annotation.BizAction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

/**
 * 用户登录
 */
@RestController
@RequestMapping("/user_login")
public interface UserLoginController {
    /**
     * 获取授权码（Authorization Code）
     * 1、发现用户未登录，返回 303，通过浏览器重定向到登录页面
     * 2、用户已登录，于是执行授权逻辑，签发授权码
     *
     * @param responseType 该值只能是 code
     * @param clientId     应用 id
     * @param redirectUri  重定向地址
     * @param scope        权限范围
     * @param state        不透明字符串
     * @param webUrl       前端页面地址，用于跳到这里以便获取 Token
     * @param req          请求对象
     * @param resp         响应对象
     */
    @GetMapping("/authorization")
    @BizAction("获取授权码")
    @AllowOpenAccess
    void authorizationCode(@RequestParam("response_type") String responseType,
                           @RequestParam("client_id") String clientId,
                           @RequestParam("redirect_uri") String redirectUri,
                           @RequestParam(required = false) String scope,
                           @RequestParam String state,
                           @RequestParam(value = "web_url", required = false) String webUrl,
                           HttpServletRequest req, HttpServletResponse resp);

    /**
     * 获取 Token
     *
     * @param authorization client 信息
     * @param grantType     授权码流程
     * @param code          授权码
     * @param state         不透明字符串
     * @param webUrl        前端页面地址，用于跳到这里以便获取 Token
     * @return 令牌 Token
     */
    @PostMapping("/token")
    @AllowOpenAccess
    @BizAction("获取 Token")
    JwtToken token(@RequestHeader String authorization, @RequestParam("grant_type") String grantType,
                   @RequestParam String code, @RequestParam String state,
                   @RequestParam(value = "web_url", required = false) String webUrl);

    /**
     * 通过 Refresh Token 刷新 Access Token
     * 这是通过头传输 client_id/client_secret
     *
     * @param grantType    必选，固定为 refresh_token
     * @param refreshToken 必选，Refresh Token
     * @return Token
     */
    @PostMapping("/refresh_token")
    @ClientAuthentication
    @BizAction("通过 Refresh Token 刷新 Access Token")
    JwtToken refreshToken(@RequestParam("grant_type") String grantType, @RequestParam("refresh_token") String refreshToken);

    /**
     * 传统 Web 用户登录
     *
     * @param username 用户名/手机号/邮箱
     * @param password 密码
     * @param appId    应用 id
     * @return 用户的 JWT Token
     */
    @PostMapping("/web")
    @AllowOpenAccess
    @ImageCaptchaCheck
    @BizAction("用户登录")
    JwtToken loginWeb(@RequestParam String username, @RequestParam String password, @RequestParam String appId);

    /**
     * 通过客户端认证的用户登录
     *
     * @param username 用户名/手机号/邮箱
     * @param password 密码
     * @return 用户的 JWT Token
     */
    @PostMapping
    @ClientAuthentication
    @BizAction("用户登录")
    JwtToken login(@RequestParam String username, @RequestParam String password);

    /**
     * 用户登录
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
    JwtToken ropcToken(@RequestParam("grant_type") String grantType,
                       @RequestParam String username, @RequestParam String password,
                       @RequestParam("client_id") String clientId,
                       @RequestParam("client_secret") String clientSecret,
                       @RequestParam(required = false) String scope);

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    @BizAction("用户登出")
    @AllowOpenAccess
    boolean logout(@RequestParam(required = false) String returnUrl, HttpServletResponse resp, HttpSession session);
}
