---
title: 注册、登录
subTitle: 2024-12-05 by Frank Cheung
description: 注册、登录
date: 2022-01-05
tags:
  - 注册
  - 登录
layout: layouts/docs.njk
---

# 用户的注册、登录

# Logout 登出

登出方式有两种，视乎你的业务场景而定。

| 单点登出                      | 全局登出                                    |
|---------------------------|-----------------------------------------|
| 当前域下的用户身份退出               | 注销 Token。当用户在一个应用中登出时，自动从所有已登录的关联应用中登出。 |
| 集成到 SDK 中，调业务方的 logout 接口 | IAM 接口                                  |

由于 Cookie 是`HttpOnly`的，前端 JavaScript 无法读取或删除它，所以必须通过 后端接口 来清除。

Client 内置登出接口：

```
GET \user\logout
```

即使单点登出用户后，IAM 仍保留用户的登录状态，此时 Client 再登录的话即可立即”无感知“登录，不用输入账号密码。