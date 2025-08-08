---
title: 开发环境的准备
subTitle: 2024-12-05 by Frank Cheung
description: 开发环境的准备
date: 2022-01-05
tags:
  - dev
layout: layouts/docs-cn.njk
---
# 开发环境的准备

本文阐述了基于 Node 大型前端开发环境配搭 AJ-IAM 在开发者本地的准备，一般是 VSCode + Eclipse/IDEA + Nginx 的环境。
假设你的前端程序是 React/VueJs 的大型前端环境，那么本文正好适合你。下面介绍所需的工具环境。

- VS Code 或其他前端，基于 Node/NPM 的前端开发环境，开发阶段透过 Node 代理后台接口
- Eclipse/IDEA 或其他后端，基于 SpringBoot 的后端开发环境
- 如果需要多租户开发，那么需要 Nginx 配置多租户（代理后台 API）


相对于纯 HTML 前端（打包后的 React/Vue 亦算），开发环境的配置是比较复杂的，所以请耐心陪同我在下面的教程中了解如何搭建。、

## 下载源码运行 IAM
这一步就不详细说了，执行`main()`函数运行 SpringBoot 程序，端口号默认是 8082。
访问接口`http://localhost:8082/iam_api/`返回 JSON：

```json
{
    "status": 1,
    "errorCode": null,
    "traceId": "E9395E4765E244CE8080A44137296E4D",
    "message": null,
    "data": "Hello World"
}
```

该接口仅仅是用于测试的接口，只返回 Hello World。

## 域名映射 IP
就是修改本地 hosts 文件，将域名映射到 IP。下面以 windows 系统为例子，打开`C:\Windows\System32\drivers\etc\hosts`文件。
增加一笔域名的映射，例如：

```
127.0.0.1       local.foo.com
```

## Nginx

```
server {
    listen       80;
    server_name  local.foo.com;

    location / { # 登录页面
        ssi on;
        alias D:/sp42/code/ajaxjs/aj-iam/aj-iam-admin/;  # 注意路径斜杠
        index index.html index.htm; # 默认首页
        try_files $uri $uri/ /index.html; # 支持单页应用（SPA）路由
        autoindex on;  # 可选，显示目录列表
    }

    location /iam_api/ {# 代理到后端 API
        proxy_pass http://localhost:8082/iam_api/; 
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_set_header auth-tenant-id 8; # 添加租户ID
    }      
}
```
这样加了第一层的代理，就可以`local.foo.com`访问 IAM 接口，在 Postman 里面测试下。

![](/asset/imgs/nginx-iam.jpg)

## Client 配置

IAM 配置好之后，将 IAM 访问地址配置到 Client 端，所谓 Client 就是你的业务系统，整合了 iam-client SDK 的。

在 yaml 文件中配置`auth.iam_service`及`auth.self_url`。`iam_service`是刚才我们配好的 Nginx 代理地址；`self_url`是 client 就是业务系统本身的地址，
因为当前我们在 nodejs 的开发环境中，须使用代理的地址：`http://localhost:3000`而不是在 IDEA 运行的那个地址

![](/asset/imgs/client-iam.jpg)

配置好之后 Client 运行起来，让前端可以访问。


# 访问 IAM User API

## 问题的出现

像 IAM 这样的认证中心，管理着用户相关的接口，比如获取用户信息，修改用户信息，修改用户密码等的接口。
认证中心是独立的进程和有自己的域 Domain，业务系统本身没有用户管理功能，而是通过 IAM SDK 与 IAM 整合。
当业务系统需要获取用户信息，修改用户信息，修改用户密码的时候，应该怎么和 IAM 整合比较好，
因为涉及跨域调用，这样的话把 AccessToken 存储在 httponly 的 cookie 则无用，但又不能显式存储 Token 在 localStorage 这类地方（安全性的缘故）。

我想到一个方法，就是在业务系统搞一个类似 Nginx 的转发代理，
专门针对 获取用户信息，修改用户信息，修改用户密码等的业务，转发到 IAM，把本地的认证信息（cookie 传入）提取 token 再转发到 IAM。

实际上这属于BFF（Backend For Frontend）/后端代理的思路。
## 开发环境

这里以 Vue3 的 Vite 开发环境为例子，其他 React 的大同小异。
首先规定前端访问前缀的`/rdd/rdd_api/iam_user`都是访问 IAM User API的，那么接着就在 Vite 添加一条路由转发规则：

```js
const init: ProxyTargetList = {
    "/rdd/rdd_api/iam_user": iamProxyOptions as ProxyTargetList,
    [env.VITE_APP_BASE_API]: commonProxyOptions as ProxyTargetList, // 这是业务系统 API
}
```

注意顺序越靠前的优先级越高，IAM 的在非 IAM 签名前面。再看看 IAM 的代理配置：

```js
  let iamProxyOptions: ProxyOptions = {
    target: env.VITE_APP_IAM_URL, // 'http://local.foo.com/iam_api'
    changeOrigin: true,
    rewrite: (path: string) => {
      return path.replace(/\/rdd\/rdd_api\/iam_user/, '');
    },
  };
```

开始以为 Cookie 的`SameSite = strict`限制跨域，IAM API 不能接受，但实际测试发现，Cookie 也可以直接传，不需要转换。

如果 IAM API 不能接受，那么也可以通过 Vite 的`configure`的`proxy.on('proxyReq')`事件转换 Cookie 为 HTTP Head 给 IAM。

## 线上环境
这里注意两点，原先 IAM 的 HTTPS 经过 Nginx 转发不行，于是改用非 HTTPS 的；另外 HOST 已强制指定，否则 404。

```
location /rdd/rdd_api/iam_user/ { # 代理 IAM User
    proxy_pass http://iamtest.ajaxjs.com/iam_api/; # 不要 SSL，证书不行
    proxy_set_header Host iamtest.ajaxjs.com; # 自定义 host
}
```
开始以为 Cookie 的`SameSite = strict`限制跨域，IAM API 不能接受，但实际测试发现，Cookie 也可以直接传，不需要转换。

如果 IAM API 不能接受，那么也可以通过 Nginx 转换 Cookie 为 HTTP Head 给 IAM。
