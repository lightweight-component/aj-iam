---
title: Refresh Token
subTitle: 2024-12-05 by Frank Cheung
description: Refresh Token
date: 2022-01-05
tags:
  - Refresh Token
layout: layouts/docs.njk
---
# Refresh Token

当用户在登录成功之后，服务端生成并返回两个 token 给客户端，其中 token 1 是用户执行业务请求访问服务器的时候使用，假设设置过期时间为 24 小时； token 2 是用于给的 token 1和 token 2 续期的时候使用，假设设置过期时间为 48 小时，如下图示所示：

![](wechat_2025-08-19_144808_986.jpg)

当 token 1过期之后开始启用 token 2，客户端携带 token 2请求服务器刷新 token ，服务器针对 token 2有如下的执行逻辑：

![](refresh-token.jpg)

（1）如果服务端验证 token 2 没有过期，那认定用户还是活跃用户，如下图所示时间点：

![](wechat_2025-08-19_145010_538.jpg)

此时服务端正常的生成并返回两个新的 token （ token 1 和 token 2，并且设置对应的过期时间），此时用户还可以请求服务器执行业务逻辑。

（2）如果 token 2刷新的时候，服务端发现 token 2也过期了，如下图所示的时间点：

![](wechat_2025-08-19_145214_554.jpg)

这个时候，我们就需要强制要求用户重新的登录系统，服务器再颁发两个新的 token 给客户端。

通过双 token 的验证方式是可以有效地区分用户是活跃用户还是非活跃用户，给活跃的用户可以自动的续期，对于非活动的用户需要再次执行登录操作。

