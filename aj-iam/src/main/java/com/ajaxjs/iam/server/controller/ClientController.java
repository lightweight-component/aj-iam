package com.ajaxjs.iam.server.controller;

import com.ajaxjs.iam.UserConstants;
import com.ajaxjs.iam.client.BaseOidcClientUserController;
import com.ajaxjs.iam.jwt.JwtAccessToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * IAM 作为 OIDC 客户端的相关业务
 */
@RestController
@RequestMapping("/client")
public class ClientController extends BaseOidcClientUserController {
    @Override
    public JwtAccessToken onAccessTokenGot(JwtAccessToken token, HttpServletResponse resp, HttpSession session) {
        String tokenStr = token.getId_token();
        // 设置 Token 到 Cookie
        ResponseCookie cookie = ResponseCookie.from(UserConstants.ACCESS_TOKEN_KEY, tokenStr)
                .httpOnly(true)
                .secure(false) // TODO for prod
                .path("/")
                .sameSite("Strict") // 设置 SameSite 属性
                .maxAge(3600 * 24 * 3) // 有效期 1 小时
                .build();

        resp.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return token;
    }

    @GetMapping("/to_login")
    public RedirectView loginPageUrl(HttpSession session, @RequestParam(required = false) String web_url) {
        return loginPageUrl(session, "/iam_api/oidc/authorization", getClientId(), "/iam_api/client/callback", web_url);
    }

    @GetMapping("/callback")
    public ModelAndView callbackToken(@RequestParam String code, @RequestParam String state, @RequestParam(required = false) String web_url, HttpSession session, HttpServletResponse resp) {
        return callbackToken(getClientId(), getClientSecret(), code, state, web_url, session, resp);
    }

}
