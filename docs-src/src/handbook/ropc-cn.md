---
title: 资源所有者密码凭证（ROPC）
subTitle: 2024-12-05 by Frank Cheung
description: 资源所有者密码凭证（ROPC）
date: 2022-01-05
tags:
  - ROPC
layout: layouts/docs-cn.njk
---
# 资源所有者密码凭证（ROPC）

OAuth 规范中除了常见的授权模式外，还有一种密码凭证模式，即 Resource Owner Password Credentials（ROPC）。
这种模式只能在高度信任的情况下使用，说白了就是登录页不与 IAM Server 在一起，而是跟业务系统一起。这是一种不得已的情况，一般不推荐，在新版 OAuth 中被弃用。

虽然但是，有时候还是需要的。那么我们来看看 ROPC 在 IAM 中怎办使用。

首先在你应用程序本地设置控制器：

```java
import com.ajaxjs.iam.annotation.AllowAccess;
import com.zoomtech2008.rdd.model.dto.RddUserDTO;
import com.zoomtech2008.rdd.model.vo.RddUserTokenVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用戶
 */
@RestController
@RequestMapping("/user")
public interface UserController {
    /**
     * 注册用戶
     *
     * @param user 用戶
     * @return 是否成功
     */
    @PostMapping
    @AllowAccess
    boolean register(@RequestBody RddUserDTO user);

    /**
     * 登录 
     * 
     * @param user 用戶
     */
    @PostMapping("/login")
    RddUserTokenVo login(@RequestBody RddUserDTO user);

    /**
     * 登出
     *
     * @return 是否成功
     */
    @PostMapping("/logout")
    @AllowAccess
    boolean logout();
}
```

实现它。

```java
import com.ajaxjs.framework.database.IgnoreDataBaseConnect;
import com.ajaxjs.iam.annotation.AllowAccess;
import com.ajaxjs.iam.client.BaseOidcClientUserController;
import com.ajaxjs.iam.jwt.JWebToken;
import com.ajaxjs.iam.jwt.JWebTokenMgr;
import com.ajaxjs.iam.jwt.JwtAccessToken;
import com.zoomtech2008.rdd.controller.UserController;
import com.zoomtech2008.rdd.model.dto.RddUserDTO;
import com.zoomtech2008.rdd.model.vo.RddUserTokenVo;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Service
public class UserService extends BaseOidcClientUserController implements UserController {
    @Override
    public boolean register(RddUserDTO user) {
        throw new UnsupportedOperationException("请直接调用 IAM 接口");
    }

    @Override
    @IgnoreDataBaseConnect
    public RddUserTokenVo login(RddUserDTO user) {
        JwtAccessToken token = ropcLogin(user.getUsername(), user.getPassword());

        // u can get the user info. via the token by HTTP API, but JWT token contains that user info.
        JWebTokenMgr mgr = new JWebTokenMgr();
        JWebToken jwt = mgr.parse(token.getId_token());

        RddUserTokenVo vo = new RddUserTokenVo();
        vo.setToken(token.getId_token());
        vo.setUserId(jwt.getPayload().getSub());

        return vo;
    }

    @Override
    @IgnoreDataBaseConnect
    @AllowAccess
    public boolean logout() {
        return true;
    }

    @Override
    public JwtAccessToken onAccessTokenGot(JwtAccessToken token, HttpServletResponse resp, HttpSession session) {
        return null;
    }
}
```

如上主要是调用 IAM-SDK 的`ropcLogin()`获取 Token 返回返回。
整个过程还是比较简单直接的，远没有授权码那样多次跳转那么麻烦（当然牺牲了安全性）。