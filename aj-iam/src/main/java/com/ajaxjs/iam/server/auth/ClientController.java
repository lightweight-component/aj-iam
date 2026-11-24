package com.ajaxjs.iam.server.auth;

import com.ajaxjs.iam.annotation.AllowOpenAccess;
import com.ajaxjs.iam.client.BaseOidcClientUserController;
import com.ajaxjs.iam.client.CacheProvider;
import com.ajaxjs.iam.jwt.JwtToken;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * IAM 作为 OIDC 客户端的相关业务
 */
@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController extends BaseOidcClientUserController {
    private final CacheProvider cacheProvider;

    @Override
    public CacheProvider getCacheProvider() {
        return cacheProvider;
    }

    @Override
    public JwtToken onAccessTokenGot(JwtToken token, HttpServletResponse resp) {
        setTokenToCookie(token, resp);

        return token;
    }

    @AllowOpenAccess
    @GetMapping("/to_login")
    public RedirectView loginPageUrl(@RequestParam(required = false) String web_url) {
        return loginPageUrl("/iam_api/user_login/authorization", "/iam_api/client/callback", web_url);
    }

    @AllowOpenAccess
    @GetMapping("/callback")
    public ModelAndView callbackToken(@RequestParam String code, @RequestParam String state, @RequestParam(required = false) String web_url, HttpServletResponse resp) {
        return callbackToken(getClientId(), getClientSecret(), code, state, web_url, resp);
    }
}
