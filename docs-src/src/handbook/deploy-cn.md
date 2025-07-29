---
title: 部署 IAM 程序
subTitle: 2024-12-05 by Frank Cheung
description: 部署 IAM 程序
date: 2022-01-05
tags:
  - 部署
layout: layouts/docs-cn.njk
---
# 部署 IAM 程序

我们通过源码检出和 IDEA 来完整 IAM 程序的部署，目标是 Linux 的服务器。

## 配置文件
Git checkout 源码后默认是没有任何有效的配置文件，——除了一个`application.yaml`演示文件，
位于`profiles`目录下。我们在 `profiles`目录中新建`dev`、`prod`、`test`子目录，分别对应着开发、生产、测试环境。
如不满足还可以自定义更多的子目录。

![](/asset/imgs/iam-yaml-dirs.jpg)

把`application.yaml`复制并改名`application.yml`到具体的`dev`、`prod`、`test`目录中，然后修改你具体的配置。


## 一键部署
如果你采用原始的 JAR 发布机制（而非 Jenkins/Docker/K8S），那么可以使用我们准备的“一键部署”。


# 同一个域下面工作

整合 IAM 的业务系统，建议与 IAM 放在同一个域下工作。


# 租户

IAM 通过租户`tenant`来区分用户体系。一个租户下的用户 id（`login_id`）不能重复。涉及的租户的数据库表、API 接口均有保存`tenant_id`的字段或入参。
怎么入参比较优雅呢（相对于调接口都要传参`tenant_id`）？我们采用域名区分的方法来划分租户。

- 首先分配一个域名给某个租户，例如 bar.foo.com 但不是根据这个域名关联那个租户
- 在 Nginx 代理中加入自定义头`auth-tenant-id`，表示某个组件，这样就是能在每个接口中获取租户了