---
title: OIDC 授权码登录接入
description: AJ-IAM 管理端的授权码登录、后端换令牌、Cookie 和代理配置
tags:
  - 登录
  - OIDC
layout: layouts/docs.njk
---

# OIDC 授权码登录接入

本文以 AJ-IAM 管理端为例。线上管理端部署在 `https://wyndme.cn/admin/`，同域提供 `/iam_login/` 登录页和 `/iam_api/` 接口。本地开发时，Vite 将登录页和接口转发到线上 IAM，浏览器仍停留在 `http://localhost:5173/`。

## 从前端发起登录

管理端启动时通过 `GET /iam_admin_api/curr_user` 获取当前用户。收到 HTTP 401 时提示用户登录；用户确认后，浏览器导航到 `/iam_api/client/to_login`：

```ts
const params = new URLSearchParams({
    web_url: import.meta.env.DEV ? '/' : '/admin/',
});
location.assign('/iam_api/client/to_login?' + params);
```

`web_url` 是本项目用于登录后返回管理端的参数。它不同于 `redirect_uri`：后者指接收授权码的后端回调。开发环境返回 `/`，线上返回 `/admin/`。

1. `ClientController` 的 `/client/to_login` 调用 `BaseOidcClientUserController.loginPageUrl()`，生成并缓存 `state`，然后重定向到 `/iam_api/user_login/authorization`。当前请求携带 `response_type=code`、`client_id`、`redirect_uri=/iam_api/client/callback`、`state` 和 `web_url`。
2. 如果 IAM 会话还没有登录用户，授权接口优先读取应用的 `app.login_page`，没有配置时按请求租户读取 `tenant.login_page`。管理端的登录页地址使用 `../../iam_login/`，浏览器解析为同源的 `/iam_login/`，原授权参数随之带到登录页。
3. 登录页用 `GET /iam_api/captcha?uuid=...` 加载图片验证码，并向 `POST /iam_api/user_login` 提交表单参数 `username`、`password`、`appId`、`uuid` 和 `captcha_code`。当前页面固定使用 `appId=lKi9p9FyicBd6eA`，需与后端的 `auth.clientId` 一致。密码登录按该应用的 `tenant_id` 查询用户，因此还要核对应用与登录账号所属的租户。
4. 登录成功后，IAM 将用户保存到会话。登录页带着原授权参数重新访问 `/user_login/authorization`；已有 IAM 会话时，授权接口可以直接签发授权码。浏览器随后收到 303，访问 `/iam_api/client/callback?code=...&state=...&web_url=...`。

## 后端回调用表单换 Token

`/client/to_login`、`/client/callback` 和 IAM 的授权、令牌两个端点允许未持有管理端令牌的请求进入。回调从缓存取出此前保存的 `state`，匹配后删除，再由服务端用授权码换 Token。失败后从登录入口重新发起，避免重放已经消费的回调。

服务端配置示例：

```yaml
auth:
  iam_service: https://wyndme.cn/iam_api
  clientId: lKi9p9FyicBd6eA
  clientSecret: ${IAM_CLIENT_SECRET}
```

`auth.iam_service` 可带 `/iam_api`，代码会避免重复拼接。实际请求为：

```http
POST /iam_api/user_login/token HTTP/1.1
Host: wyndme.cn
Authorization: Basic <客户端凭据的 Base64 编码>
Content-Type: application/x-www-form-urlencoded

grant_type=authorization_code&code=<表单编码后的授权码>&state=<表单编码后的state>
```

客户端凭据由后端用 `OAuthTools.encodeClient(clientId, clientSecret)` 生成。Java 客户端使用 `Post.form`，因为控制器通过 `@RequestParam` 读取 `grant_type`、`code` 和 `state`。`Post.api` 发送的是 JSON 请求体，无法满足这个接口的参数绑定方式。响应仍按 JSON 解析。当前接口要求在换令牌时提交 `state`，浏览器不直接调用该接口，也不持有 `clientSecret`。

## 回调设置 HttpOnly Cookie

兑换成功后，`ClientController.onAccessTokenGot()` 通过 `Set-Cookie` 响应头设置 `access_token`，随后回调重定向到 `web_url`。当前 Cookie 属性如下：

| 属性 | 当前值 |
| --- | --- |
| `HttpOnly` | 开启。JavaScript 不能读取，浏览器仍会自动发送 |
| `Path` | `/`，覆盖管理端和 API 路径 |
| `SameSite` | `Strict` |
| `Max-Age` | 259200 秒（3 天）；JWT 自身的过期时间仍由服务端校验 |
| `Secure` | 当前未开启，线上 HTTPS 部署应开启 |

管理端返回后重新请求 `/iam_admin_api/curr_user`，用接口响应更新登录状态。前端不需要把 Token 写入 `localStorage`，也不应通过 URL 传递 Token。

## 线上 Nginx

将 `aj-iam-ui/dist/` 部署到 `/home/static-html/aj-iam-admin/`，将 `aj-iam-login/` 部署到 `/home/static-html/iam-login/`。以下配置放在同一个 HTTPS `server` 中；示例租户 ID 为 `9`：

```nginx
location /admin/ {
    alias /home/static-html/aj-iam-admin/;
    index index.html;
}

location = /iam_login {
    return 301 /iam_login/$is_args$args;
}

location /iam_login/ {
    alias /home/static-html/iam-login/;
    index index.html;
}

location /iam_api/ {
    proxy_pass http://127.0.0.1:8082/iam_api/;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
    proxy_set_header auth-tenant-id 9;
}

location /iam_admin_api/ {
    proxy_pass http://127.0.0.1:8082/iam_api/;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
    proxy_set_header admin-protection on;
}
```

补尾斜杠时用 `$is_args$args` 保留授权参数。配置修改后先执行 `nginx -t`，通过后再重载。Nginx 的租户头供按请求解析租户的逻辑使用，不能代替密码登录所用的应用租户配置。

## Vite 开发代理

下面是 `aj-iam-ui/vite.config.ts` 的代理部分。转发登录页本身，才能在本地同源加载 HTML、样式和验证码，并让回调设置的 Cookie 用于后续 API 请求。

```ts
const iamProxy = {
  target: 'https://wyndme.cn',
  changeOrigin: true,
  autoRewrite: true,
  protocolRewrite: 'http',
  cookieDomainRewrite: '',
};

// defineConfig({ server: { proxy } })
const proxy = {
  '^/iam_api(?=/|\\?|$)': iamProxy,
  '^/iam_login(?=/|\\?|$)': iamProxy,
  '^/iam_admin_api(?=/|\\?|$)': {
    ...iamProxy,
    rewrite: (path: string) => path.replace(/^\/iam_admin_api/, '/iam_api'),
  },
};
```

代理会把匹配线上目标主机的重定向改回开发服务，并移除上游 Cookie 的 `Domain` 属性。漏配 `/iam_api` 时，Vite 可能把登录入口当成 SPA 路径并返回首页，造成循环跳转；漏掉 `/iam_admin_api` 的路径重写时，当前用户接口可能返回 404，无法触发 401 登录提示。使用这套配置时，本地管理端的 API 操作也会作用于线上数据。

## 协议边界

本文记录的是项目当前跑通的授权码流程。现有实现还有自定义的 `web_url` 和换令牌时的 `state`，不能直接视为完整的标准 OIDC Provider。对外提供通用 OIDC 接入前，应补齐已登记 `redirect_uri` 的精确匹配、PKCE、与客户端和回调绑定的单次授权码，以及独立签发和验证的 ID Token；同时应校验登录后的返回地址，并将 `state` 与发起登录的浏览器会话绑定。
