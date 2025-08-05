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

