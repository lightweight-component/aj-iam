-- ============================================================
-- OAuth Provider API Endpoint Data
-- Generated from com.ajaxjs.oauth.impl package
-- ============================================================

-- 1. 阿里云
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('阿里云', NULL, 'https://signin.aliyun.com/oauth2/v1/auth', 'https://oauth.aliyun.com/v1/token', 'https://oauth.aliyun.com/v1/userinfo', NULL, 'https://oauth.aliyun.com/v1/token');

-- 2. CSDN 博客平台
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('CSDN 博客平台', NULL, 'https://api.csdn.net/oauth2/authorize', 'https://api.csdn.net/oauth2/access_token', 'https://api.csdn.net/user/getinfo', NULL, NULL);

-- 3. 钉钉账号登录
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('钉钉账号登录', NULL, 'https://oapi.dingtalk.com/connect/oauth2/sns_authorize', "TODO", 'https://oapi.dingtalk.com/sns/getuserinfo_bycode', NULL, NULL);

-- 4. 钉钉扫码登录
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('钉钉扫码登录', NULL, 'https://oapi.dingtalk.com/connect/qrconnect', "TODO", 'https://oapi.dingtalk.com/sns/getuserinfo_bycode', NULL, NULL);

-- 5. 新版钉钉扫码登录
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('新版钉钉扫码登录', NULL, 'https://login.dingtalk.com/oauth2/challenge.htm', 'https://api.dingtalk.com/v1.0/oauth2/userAccessToken', 'https://api.dingtalk.com/v1.0/contact/users/me', NULL, NULL);

-- 6. 饿了么
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('饿了么', NULL, 'https://open-api.shop.ele.me/authorize', 'https://open-api.shop.ele.me/token', 'https://open-api.shop.ele.me/api/v1/', NULL, 'https://open-api.shop.ele.me/token');

-- 7. 飞书平台
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('飞书平台', '企业自建应用授权登录，原逻辑由 beacon 集成于 1.14.0 版，但最新的飞书 api 已修改，并且飞书平台一直为 Deprecated 状态。所以，最终修改该平台的实际发布版本为 1.15.9', 'https://open.feishu.cn/open-apis/authen/v1/index', 'https://open.feishu.cn/open-apis/authen/v1/access_token', 'https://open.feishu.cn/open-apis/authen/v1/user_info', NULL, 'https://open.feishu.cn/open-apis/authen/v1/refresh_access_token');

-- 8. 美团外卖
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('美团外卖', NULL, 'https://openapi.waimai.meituan.com/oauth/authorize', 'https://openapi.waimai.meituan.com/oauth/access_token', 'https://openapi.waimai.meituan.com/oauth/userinfo', NULL, 'https://openapi.waimai.meituan.com/oauth/refresh_token');

-- 9. 微软中国
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('微软中国', NULL, 'https://login.partner.microsoftonline.cn/%s/oauth2/v2.0/authorize', 'https://login.partner.microsoftonline.cn/%s/oauth2/v2.0/token', 'https://microsoftgraph.chinacloudapi.cn/v1.0/me', NULL, 'https://login.partner.microsoftonline.cn/%s/oauth2/v2.0/token');

-- 10. 开源中国
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('开源中国', NULL, 'https://www.oschina.net/action/oauth2/authorize', 'https://www.oschina.net/action/openapi/token', 'https://www.oschina.net/action/openapi/user', NULL, NULL);

-- 11. 淘宝
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('淘宝', NULL, 'https://oauth.taobao.com/authorize', 'https://oauth.taobao.com/token', "TODO", NULL, NULL);

-- 12. 喜马拉雅
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('喜马拉雅', NULL, 'https://api.ximalaya.com/oauth2/js/authorize', 'https://api.ximalaya.com/oauth2/v2/access_token', 'https://api.ximalaya.com/profile/user_info', NULL, 'https://oauth.aliyun.com/v1/token');

-- 13. 支付宝
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('支付宝', NULL, 'https://openauth.alipay.com/oauth2/publicAppAuthorize.htm', 'https://openapi.alipay.com/gateway.do', 'https://openapi.alipay.com/gateway.do', NULL, NULL);

-- 14. Amazon
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('Amazon', NULL, 'https://www.amazon.com/ap/oa', 'https://api.amazon.com/auth/o2/token', 'https://api.amazon.com/user/profile', NULL, 'https://api.amazon.com/auth/o2/token');

-- 15. Apple
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('Apple', NULL, 'https://appleid.apple.com/auth/authorize', 'https://appleid.apple.com/auth/token', "TODO", NULL, NULL);

-- 16. 百度
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('百度', NULL, 'https://openapi.baidu.com/oauth/2.0/authorize', 'https://openapi.baidu.com/oauth/2.0/token', 'https://openapi.baidu.com/rest/2.0/passport/users/getInfo', 'https://openapi.baidu.com/rest/2.0/passport/auth/revokeAuthorization', 'https://openapi.baidu.com/oauth/2.0/token');

-- 17. 抖音
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('抖音', NULL, 'https://open.douyin.com/platform/oauth/connect', 'https://open.douyin.com/oauth/access_token/', 'https://open.douyin.com/oauth/userinfo/', NULL, 'https://open.douyin.com/oauth/refresh_token/');

-- 18. Facebook
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('Facebook', NULL, 'https://www.facebook.com/v18.0/dialog/oauth', 'https://graph.facebook.com/v18.0/oauth/access_token', 'https://graph.facebook.com/v18.0/me', NULL, NULL);

-- 19. Gitee
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('Gitee', NULL, 'https://gitee.com/oauth/authorize', 'https://gitee.com/oauth/token', 'https://gitee.com/api/v5/user', NULL, NULL);

-- 20. GitHub
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('GitHub', NULL, 'https://github.com/login/oauth/authorize', 'https://github.com/login/oauth/access_token', 'https://api.github.com/user', NULL, NULL);

-- 21. Google
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('Google', '端点地址：https://accounts.google.com/.well-known/openid-configuration', 'https://accounts.google.com/o/oauth2/v2/auth', 'https://oauth2.googleapis.com/token', 'https://openidconnect.googleapis.com/v1/userinfo', NULL, NULL);

-- 22. 华为
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('华为', NULL, 'https://oauth-login.cloud.huawei.com/oauth2/v3/authorize', 'https://oauth-login.cloud.huawei.com/oauth2/v3/token', 'https://account.cloud.huawei.com/rest.php', NULL, 'https://oauth-login.cloud.huawei.com/oauth2/v3/token');

-- 23. 京东
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('京东', NULL, 'https://open-oauth.jd.com/oauth2/to_login', 'https://open-oauth.jd.com/oauth2/access_token', 'https://api.jd.com/routerjson', NULL, 'https://open-oauth.jd.com/oauth2/refresh_token');

-- 24. Line
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('Line', NULL, 'https://access.line.me/oauth2/v2.1/authorize', 'https://api.line.me/oauth2/v2.1/token', 'https://api.line.me/v2/profile', 'https://api.line.me/oauth2/v2.1/revoke', 'https://api.line.me/oauth2/v2.1/token');

-- 25. LinkedIn
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('LinkedIn', NULL, 'https://www.linkedin.com/oauth/v2/authorization', 'https://www.linkedin.com/oauth/v2/accessToken', 'https://api.linkedin.com/v2/me', NULL, 'https://www.linkedin.com/oauth/v2/accessToken');

-- 26. Microsoft
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('Microsoft', NULL, 'https://login.microsoftonline.com/%s/oauth2/v2.0/authorize', 'https://login.microsoftonline.com/%s/oauth2/v2.0/token', 'https://graph.microsoft.com/v1.0/me', NULL, 'https://login.microsoftonline.com/%s/oauth2/v2.0/token');

-- 27. Pinterest
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('Pinterest', NULL, 'https://api.pinterest.com/oauth', 'https://api.pinterest.com/v1/oauth/token', 'https://api.pinterest.com/v1/me', NULL, NULL);

-- 28. QQ
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('QQ', NULL, 'https://graph.qq.com/oauth2.0/authorize', 'https://graph.qq.com/oauth2.0/token', 'https://graph.qq.com/user/get_user_info', NULL, 'https://graph.qq.com/oauth2.0/token');

-- 29. Slack
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('Slack', NULL, 'https://slack.com/oauth/v2/authorize', 'https://slack.com/api/oauth.v2.access', 'https://slack.com/api/users.info', 'https://slack.com/api/auth.revoke', NULL);

-- 30. Stack Overflow
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('Stack Overflow', NULL, 'https://stackoverflow.com/oauth', 'https://stackoverflow.com/oauth/access_token/json', 'https://api.stackexchange.com/2.2/me', NULL, NULL);

-- 31. 今日头条
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('今日头条', NULL, 'https://open.snssdk.com/auth/authorize', 'https://open.snssdk.com/auth/token', 'https://open.snssdk.com/data/user_profile', NULL, NULL);

-- 32. Twitter
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('Twitter', NULL, 'https://api.twitter.com/oauth/authenticate', 'https://api.twitter.com/oauth/access_token', 'https://api.twitter.com/1.1/account/verify_credentials.json', NULL, NULL);

-- 33. 微博
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('微博', NULL, 'https://api.weibo.com/oauth2/authorize', 'https://api.weibo.com/oauth2/access_token', 'https://api.weibo.com/2/users/show.json', 'https://api.weibo.com/oauth2/revokeoauth2', NULL);

-- 34. 企业微信二维码登录
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('企业微信二维码登录', NULL, 'https://open.work.weixin.qq.com/wwopen/sso/qrConnect', 'https://qyapi.weixin.qq.com/cgi-bin/gettoken', 'https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo', NULL, NULL);

-- 35. 微信公众平台
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('微信公众平台', NULL, 'https://open.weixin.qq.com/connect/oauth2/authorize', 'https://api.weixin.qq.com/sns/oauth2/access_token', 'https://api.weixin.qq.com/sns/userinfo', NULL, 'https://api.weixin.qq.com/sns/oauth2/refresh_token');

-- 36. 微信开放平台
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('微信开放平台', NULL, 'https://open.weixin.qq.com/connect/qrconnect', 'https://api.weixin.qq.com/sns/oauth2/access_token', 'https://api.weixin.qq.com/sns/userinfo', NULL, 'https://api.weixin.qq.com/sns/oauth2/refresh_token');

-- 37. 小米
INSERT INTO `oauth_provider` (`name`, `content`, `authorize_api`, `access_token_api`, `user_info_api`, `revoke_api`, `refresh_api`) VALUES
('小米', NULL, 'https://account.xiaomi.com/oauth2/authorize', 'https://account.xiaomi.com/oauth2/token', 'https://open.account.xiaomi.com/user/profile', NULL, 'https://account.xiaomi.com/oauth2/token');


-- ============================================================
-- OAuth Provider Scope Data
-- ============================================================

-- 14. Amazon
INSERT INTO `oauth_provider_scope` (`provider_id`, `provider_name`, `scope`, `description`, `is_default`, `memo`) VALUES
(14, 'Amazon', 'profile', 'The profile scope includes a user''s name and email address', 1, NULL),
(14, 'Amazon', 'profile:user_id', 'The profile:user_id scope only includes the user_id field of the profile', 1, NULL),
(14, 'Amazon', 'postal_code', 'This includes the user''s zip/postal code number from their primary shipping address', 1, NULL);

-- 15. Apple
INSERT INTO `oauth_provider_scope` (`provider_id`, `provider_name`, `scope`, `description`, `is_default`, `memo`) VALUES
(15, 'Apple', 'email', '用户邮箱', 1, NULL),
(15, 'Apple', 'name', '用户名', 1, NULL);

-- 16. 百度
INSERT INTO `oauth_provider_scope` (`provider_id`, `provider_name`, `scope`, `description`, `is_default`, `memo`) VALUES
(16, '百度', 'basic', '用户基本权限，可以获取用户的基本信息 。', 1, 'scope 含义，以 description 为准'),
(16, '百度', 'super_msg', '往用户的百度首页上发送消息提醒，相关API任何应用都能使用，但要想将消息提醒在百度首页显示，需要第三方在注册应用时额外填写相关信息。', 0, NULL),
(16, '百度', 'netdisk', '获取用户在个人云存储中存放的数据。', 0, NULL),
(16, '百度', 'public', '可以访问公共的开放API。', 0, NULL),
(16, '百度', 'hao123', '可以访问Hao123 提供的开放API接口。该权限需要申请开通，请将具体的理由和用途发邮件给tuangou@baidu.com。', 0, NULL);

-- 17. 抖音
INSERT INTO `oauth_provider_scope` (`provider_id`, `provider_name`, `scope`, `description`, `is_default`, `memo`) VALUES
(17, '抖音', 'user_info', '返回抖音用户公开信息', 1, '无需申请 默认开启'),
(17, '抖音', 'aweme.share', '抖音分享', 0, '无需申请 默认开启'),
(17, '抖音', 'im.share', '分享给抖音好友', 0, '普通权限,管理中心申请'),
(17, '抖音', 'renew_refresh_token', '授权有效期动态续期', 0, NULL),
(17, '抖音', 'following.list', '获取该用户的关注列表', 0, NULL),
(17, '抖音', 'fans.list', '获取该用户的粉丝列表', 0, NULL),
(17, '抖音', 'video.create', '视频发布及管理', 0, NULL),
(17, '抖音', 'video.delete', '删除内容', 0, NULL),
(17, '抖音', 'video.data', '查询授权用户的抖音视频数据', 0, NULL),
(17, '抖音', 'video.list', '查询特定抖音视频的视频数据', 0, NULL),
(17, '抖音', 'share_with_source', '分享携带来源标签，用户可点击标签进入转化页', 0, '特殊权限 默认关闭 管理中心申请'),
(17, '抖音', 'mobile', '用抖音帐号登录第三方平台，获得用户在抖音上的手机号码', 0, NULL),
(17, '抖音', 'mobile_alert', '用抖音帐号登录第三方平台，获得用户在抖音上的手机号码', 0, NULL),
(17, '抖音', 'video.search', '关键词视频管理', 0, NULL),
(17, '抖音', 'poi.search', '查询POI信息', 0, NULL),
(17, '抖音', 'login_id', '静默授权直接获取该用户的open id', 0, NULL),
(17, '抖音', 'data.external.user', '查询用户的获赞、评论、分享，主页访问等相关数据', 0, '抖音数据权限, 默认关闭, 管理中心申请'),
(17, '抖音', 'data.external.item', '查询作品的获赞，评论，分享等相关数据', 0, NULL),
(17, '抖音', 'fans.data', '获取用户粉丝画像数据', 0, NULL),
(17, '抖音', 'hotsearch', '获取抖音热门内容', 0, NULL),
(17, '抖音', 'star_top_score_display', '星图达人与达人对应各指数评估分，以及星图6大热门维度下的达人榜单', 0, NULL),
(17, '抖音', 'star_tops', '星图达人与达人对应各指数评估分，以及星图6大热门维度下的达人榜单', 0, NULL),
(17, '抖音', 'star_author_score_display', '星图达人与达人对应各指数评估分，以及星图6大热门维度下的达人榜单', 0, NULL),
(17, '抖音', 'data.external.sdk_share', '获取用户通过分享SDK分享视频数据', 0, NULL),
(17, '抖音', 'discovery.ent', '查询抖音电影榜、抖音剧集榜、抖音综艺榜数据', 0, '定向开通 默认关闭 定向开通');

-- 18. Facebook
INSERT INTO `oauth_provider_scope` (`provider_id`, `provider_name`, `scope`, `description`, `is_default`, `memo`) VALUES
(18, 'Facebook', 'public_profile', '权限允许应用读取用户默认的公开资料', 1, 'scope 含义，以 description 为准'),
(18, 'Facebook', 'email', '获取用户的邮箱', 0, NULL),
(18, 'Facebook', 'user_age_range', '允许应用程序访问用户的年龄范围', 0, NULL),
(18, 'Facebook', 'user_birthday', '获取用户的生日', 0, NULL),
(18, 'Facebook', 'user_friends', '获取用户的好友列表', 0, NULL),
(18, 'Facebook', 'user_gender', '获取用户的性别', 0, NULL),
(18, 'Facebook', 'user_hometown', '获取用户的家乡信息', 0, NULL),
(18, 'Facebook', 'user_likes', '获取用户的喜欢列表', 0, NULL),
(18, 'Facebook', 'user_link', '获取用户的个人链接', 1, NULL),
(18, 'Facebook', 'user_location', '获取用户的位置信息', 0, NULL),
(18, 'Facebook', 'user_photos', '获取用户的相册信息', 0, NULL),
(18, 'Facebook', 'user_posts', '获取用户发布的内容', 0, NULL),
(18, 'Facebook', 'user_videos', '获取用户上传的视频信息', 0, NULL),
(18, 'Facebook', 'groups_access_member_info', '获取公开的群组成员信息', 0, NULL),
(18, 'Facebook', 'publish_to_groups', '授权您的应用程序代表某人将内容发布到组中，前提是他们已经授予您的应用程序访问权限', 0, NULL);

-- 19. Gitee
INSERT INTO `oauth_provider_scope` (`provider_id`, `provider_name`, `scope`, `description`, `is_default`, `memo`) VALUES
(19, 'Gitee', 'user_info', '访问用户的个人信息、最新动态等', 1, 'scope 含义，以 description 为准'),
(19, 'Gitee', 'projects', '查看、创建、更新用户的项目', 0, NULL),
(19, 'Gitee', 'pull_requests', '查看、发布、更新用户的 Pull Request', 0, NULL),
(19, 'Gitee', 'issues', '查看、发布、更新用户的 Issue', 0, NULL),
(19, 'Gitee', 'notes', '查看、发布、管理用户在项目、代码片段中的评论', 0, NULL),
(19, 'Gitee', 'keys', '查看、部署、删除用户的公钥', 0, NULL),
(19, 'Gitee', 'hook', '查看、部署、更新用户的 Webhook', 0, NULL),
(19, 'Gitee', 'groups', '查看、管理用户的组织以及成员', 0, NULL),
(19, 'Gitee', 'gists', '查看、删除、更新用户的代码片段', 0, NULL),
(19, 'Gitee', 'enterprises', '查看、管理用户的企业以及成员', 0, NULL),
(19, 'Gitee', 'emails', '查看用户的个人邮箱信息', 0, NULL);

-- 20. GitHub
INSERT INTO `oauth_provider_scope` (`provider_id`, `provider_name`, `scope`, `description`, `is_default`, `memo`) VALUES
(20, 'GitHub', 'repo:status', 'Grants read/write access to public and private repository commit statuses. This scope is only necessary to grant other users or services access to private repository commit statuses without granting access to the code.', 0, 'scope 含义，以 description 为准'),
(20, 'GitHub', 'repo_deployment', 'Grants access to deployment statuses for public and private repositories. This scope is only necessary to grant other users or services access to deployment statuses, without granting access to the code.', 0, NULL),
(20, 'GitHub', 'public_repo', 'Limits access to public repositories. That includes read/write access to code, commit statuses, repository projects, collaborators, and deployment statuses for public repositories and organizations. Also required for starring public repositories.', 0, NULL),
(20, 'GitHub', 'repo:invite', 'Grants accept/decline abilities for invitations to collaborate on a repository. This scope is only necessary to grant other users or services access to invites without granting access to the code.', 0, NULL),
(20, 'GitHub', 'security_events', 'Grants read and write access to security events in the code scanning API.', 0, NULL),
(20, 'GitHub', 'write:repo_hook', 'Grants read, write, and ping access to hooks in public or private repositories.', 0, NULL),
(20, 'GitHub', 'read:repo_hook', 'Grants read and ping access to hooks in public or private repositories.', 0, NULL),
(20, 'GitHub', 'admin:org', 'Fully manage the organization and its teams, projects, and memberships.', 0, NULL),
(20, 'GitHub', 'write:org', 'Read and write access to organization membership, organization projects, and team membership.', 0, NULL),
(20, 'GitHub', 'read:org', 'Read-only access to organization membership, organization projects, and team membership.', 0, NULL),
(20, 'GitHub', 'admin:public_key', 'Fully manage public keys.', 0, NULL),
(20, 'GitHub', 'write:public_key', 'Create, list, and view details for public keys.', 0, NULL),
(20, 'GitHub', 'read:public_key', 'List and view details for public keys.', 0, NULL),
(20, 'GitHub', 'gist', 'Grants write access to gists.', 0, NULL),
(20, 'GitHub', 'notifications', 'Grants read access to a user''s notifications, mark as read access to threads, watch and unwatch access to a repository, and read, write, and delete access to thread subscriptions.', 0, NULL),
(20, 'GitHub', 'user', 'Grants read/write access to profile info only. Note that this scope includes user:email and user:follow.', 0, NULL),
(20, 'GitHub', 'read:user', 'Grants access to read a user''s profile data.', 0, NULL),
(20, 'GitHub', 'user:email', 'Grants read access to a user''s email addresses.', 0, NULL),
(20, 'GitHub', 'user:follow', 'Grants access to follow or unfollow other users.', 0, NULL),
(20, 'GitHub', 'delete_repo', 'Grants access to delete adminable repositories.', 0, NULL),
(20, 'GitHub', 'write:discussion', 'Allows read and write access for team discussions.', 0, NULL),
(20, 'GitHub', 'read:discussion', 'Allows read access for team discussions.', 0, NULL),
(20, 'GitHub', 'write:packages', 'Grants access to upload or publish a package in GitHub Packages.', 0, NULL),
(20, 'GitHub', 'read:packages', 'Grants access to download or install packages from GitHub Packages.', 0, NULL),
(20, 'GitHub', 'delete:packages', 'Grants access to delete packages from GitHub Packages.', 0, NULL),
(20, 'GitHub', 'admin:gpg_key', 'Fully manage GPG keys.', 0, NULL),
(20, 'GitHub', 'write:gpg_key', 'Create, list, and view details for GPG keys.', 0, NULL),
(20, 'GitHub', 'read:gpg_key', 'List and view details for GPG keys.', 0, NULL),
(20, 'GitHub', 'workflow', 'Grants the ability to add and update GitHub Actions workflow files. Workflow files can be committed without this scope if the same file (with both the same path and contents) exists on another branch in the same repository.', 0, NULL);

-- 21. Google
INSERT INTO `oauth_provider_scope` (`provider_id`, `provider_name`, `scope`, `description`, `is_default`, `memo`) VALUES
(21, 'Google', 'openid', 'Associate you with your personal info on Google', 1, 'scope 含义，以 description 为准'),
(21, 'Google', 'email', 'View your email address', 1, NULL),
(21, 'Google', 'profile', 'View your basic profile info', 1, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/user.phonenumbers.read', 'View your phone numbers', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/user.organization.read', 'See your education, work history and org info', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/user.gender.read', 'See your gender', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/user.emails.read', 'View your email addresses', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/user.birthday.read', 'View your complete date of birth', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/user.addresses.read', 'View your street addresses', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/userinfo.profile', 'See your personal info, including any personal info you''ve made publicly available', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/userinfo.email', 'View your email address', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/yt-analytics.readonly', 'View YouTube Analytics reports for your YouTube content', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/yt-analytics-monetary.readonly', 'View monetary and non-monetary YouTube Analytics reports for your YouTube content', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/youtubepartner-channel-audit', 'View private information of your YouTube channel relevant during the audit process with a YouTube partner', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/youtubepartner', 'View and manage your assets and associated content on YouTube', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/youtube.upload', 'Manage your YouTube videos', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/youtube.readonly', 'View your YouTube account', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/youtube.force-ssl', 'See, edit, and permanently delete your YouTube videos, ratings, comments and captions', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/youtube.channel-memberships.creator', 'See a list of your current active channel members, their current level, and when they became a member', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/youtube', 'Manage your YouTube account', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/webmasters.readonly', 'View Search Console data for your verified sites', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/webmasters', 'View and manage Search Console data for your verified sites', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/verifiedaccess', 'Verify your enterprise credentials', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/trace.append', 'Write Trace data for a project or application', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/tasks.readonly', 'View your tasks', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/tasks', 'Create, edit, organize, and delete all your tasks', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/tagmanager.readonly', 'View your Google Tag Manager container and its subcomponents', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/tagmanager.publish', 'Publish your Google Tag Manager container versions', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/tagmanager.manage.users', 'Manage user permissions of your Google Tag Manager account and container', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/tagmanager.manage.accounts', 'View and manage your Google Tag Manager accounts', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/tagmanager.edit.containerversions', 'Manage your Google Tag Manager container versions', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/tagmanager.edit.containers', 'Manage your Google Tag Manager container and its subcomponents, excluding versioning and publishing', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/tagmanager.delete.containers', 'Delete your Google Tag Manager containers', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/streetviewpublish', 'Publish and manage your 360 photos on Google Street View', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/sqlservice.admin', 'Manage your Google SQL Service instances', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/spreadsheets.readonly', 'View your Google Spreadsheets', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/spreadsheets', 'See, edit, create, and delete your spreadsheets in Google Drive', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/spanner.data', 'View and manage the contents of your Spanner databases', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/spanner.admin', 'Administer your Spanner databases', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/source.read_write', 'Manage the contents of your source code repositories', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/source.read_only', 'View the contents of your source code repositories', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/source.full_control', 'Manage your source code repositories', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/siteverification.verify_only', 'Manage your new site verifications with Google', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/siteverification', 'Manage the list of sites and domains you control', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/servicecontrol', 'Manage your Google Service Control data', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/service.management.readonly', 'View your Google API service configuration', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/service.management', 'Manage your Google API service configuration', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/script.projects.readonly', 'View Google Apps Script projects', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/script.projects', 'Create and update Google Apps Script projects', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/script.processes', 'View Google Apps Script processes', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/script.metrics', 'View Google Apps Script project''s metrics', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/script.deployments.readonly', 'View Google Apps Script deployments', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/script.deployments', 'Create and update Google Apps Script deployments', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/pubsub', 'View and manage Pub/Sub topics and subscriptions', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/presentations.readonly', 'View your Google Slides presentations', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/presentations', 'View and manage your Google Slides presentations', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/photoslibrary.sharing', 'Manage and add to shared albums on your behalf', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/photoslibrary.readonly.appcreateddata', 'Manage photos added by this app', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/photoslibrary.readonly', 'View your Google Photos library', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/photoslibrary.appendonly', 'Add to your Google Photos library', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/photoslibrary', 'View and manage your Google Photos library', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/ndev.cloudman.readonly', 'View your Google Cloud Platform management resources and deployment status information', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/ndev.cloudman', 'View and manage your Google Cloud Platform management resources and deployment status information', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/ndev.clouddns.readwrite', 'View and manage your DNS records hosted by Google Cloud DNS', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/ndev.clouddns.readonly', 'View your DNS records hosted by Google Cloud DNS', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/monitoring.write', 'Publish metric data to your Google Cloud projects', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/monitoring.read', 'View monitoring data for all of your Google Cloud and third-party projects', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/monitoring', 'View and write monitoring data for all of your Google and third-party Cloud and API projects', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/manufacturercenter', 'Manage your product listings for Google Manufacturer Center', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/logging.write', 'Submit log data for your projects', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/logging.read', 'View log data for your projects', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/logging.admin', 'Administrate log data for your projects', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/jobs', 'Manage job postings', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/indexing', 'Submit data to Google for indexing', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/groups', 'View and manage your Google Groups', 0, NULL),
(21, 'Google', 'https://mail.google.com/', 'Read, compose, send, and permanently delete all your email from Gmail', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/gmail.settings.sharing', 'Manage your sensitive mail settings, including who can manage your mail', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/gmail.settings.basic', 'Manage your basic mail settings', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/gmail.send', 'Send email on your behalf', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/gmail.readonly', 'View your email messages and settings', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/gmail.modify', 'View and modify but not delete your email', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/gmail.metadata', 'View your email message metadata such as labels and headers, but not the email body', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/gmail.labels', 'Manage mailbox labels', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/gmail.insert', 'Insert mail into your mailbox', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/gmail.compose', 'Manage drafts and send emails', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/gmail.addons.current.message.readonly', 'View your email messages when the add-on is running', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/gmail.addons.current.message.metadata', 'View your email message metadata when the add-on is running', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/gmail.addons.current.message.action', 'View your email messages when you interact with the add-on', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/gmail.addons.current.action.compose', 'Manage drafts and send emails when you interact with the add-on', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/genomics', 'View and manage Genomics data', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/games', 'Create, edit, and delete your Google Play Games activity', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/forms.currentonly', 'View and manage forms that this application has been installed in', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/forms', 'View and manage your forms in Google Drive', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/fitness.reproductive_health.write', 'See and add info about your reproductive health in Google Fit. I consent to Google sharing my reporductive health information with this app.', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/fitness.reproductive_health.read', 'See info about your reproductive health in Google Fit. I consent to Google sharing my reporductive health information with this app.', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/fitness.oxygen_saturation.write', 'See and add info about your oxygen saturation in Google Fit. I consent to Google sharing my oxygen saturation information with this app.', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/fitness.oxygen_saturation.read', 'See info about your oxygen saturation in Google Fit. I consent to Google sharing my oxygen saturation information with this app.', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/fitness.nutrition.write', 'See and add to info about your nutrition in Google Fit', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/fitness.nutrition.read', 'See info about your nutrition in Google Fit', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/fitness.location.write', 'See and add to your Google Fit location data', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/fitness.location.read', 'See your Google Fit speed and distance data', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/fitness.body_temperature.write', 'See and add to info about your body temperature in Google Fit. I consent to Google sharing my body temperature information with this app.', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/fitness.body_temperature.read', 'See info about your body temperature in Google Fit. I consent to Google sharing my body temperature information with this app.', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/fitness.body.write', 'See and add info about your body measurements and heart rate to Google Fit', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/fitness.body.read', 'See info about your body measurements and heart rate in Google Fit', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/fitness.blood_pressure.write', 'See and add info about your blood pressure in Google Fit. I consent to Google sharing my blood pressure information with this app.', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/fitness.blood_pressure.read', 'See info about your blood pressure in Google Fit. I consent to Google sharing my blood pressure information with this app.', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/fitness.blood_glucose.write', 'See and add info about your blood glucose to Google Fit. I consent to Google sharing my blood glucose information with this app.', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/fitness.blood_glucose.read', 'See info about your blood glucose in Google Fit. I consent to Google sharing my blood glucose information with this app.', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/fitness.activity.write', 'See and add to your Google Fit physical activity data', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/fitness.activity.read', 'Use Google Fit to see and store your physical activity data', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/firebase.readonly', 'View all your Firebase data and settings', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/firebase', 'View and administer all your Firebase data and settings', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/ediscovery.readonly', 'View your eDiscovery data', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/ediscovery', 'Manage your eDiscovery data', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/drive.scripts', 'Modify your Google Apps Script scripts'' behavior', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/drive.readonly', 'See and download all your Google Drive files', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/drive.photos.readonly', 'View the photos, videos and albums in your Google Photos', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/drive.metadata.readonly', 'View metadata for files in your Google Drive', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/drive.metadata', 'View and manage metadata of files in your Google Drive', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/drive.file', 'View and manage Google Drive files and folders that you have opened or created with this app', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/drive.appdata', 'View and manage its own configuration data in your Google Drive', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/drive.activity.readonly', 'View the activity record of files in your Google Drive', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/drive.activity', 'View and add to the activity record of files in your Google Drive', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/drive', 'See, edit, create, and delete all of your Google Drive files', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/activity', 'View the activity history of your Google apps', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/doubleclicksearch', 'View and manage your advertising data in DoubleClick Search', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/doubleclickbidmanager', 'View and manage your reports in DoubleClick Bid Manager', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/documents.readonly', 'View your Google Docs documents', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/documents', 'View and manage your Google Docs documents', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/display-video', 'Create, see, edit, and permanently delete your Display & Video 360 entities and reports', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/directory.readonly', 'See and download your organization''s GSuite directory', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/dialogflow', 'View, manage and query your Dialogflow agents', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/dfatrafficking', 'View and manage your DoubleClick Campaign Manager''s (DCM) display ad campaigns', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/dfareporting', 'View and manage DoubleClick for Advertisers reports', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/devstorage.read_write', 'Manage your data in Google Cloud Storage', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/devstorage.read_only', 'View your data in Google Cloud Storage', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/devstorage.full_control', 'Manage your data and permissions in Google Cloud Storage', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/ddmconversions', 'Manage DoubleClick Digital Marketing conversions', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/datastore', 'View and manage your Google Cloud Datastore data', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/content', 'Manage your product listings and accounts for Google Shopping', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/contacts.readonly', 'See and download your contacts', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/contacts.other.readonly', 'See and download contact info automatically saved in your "Other contacts"', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/contacts', 'See, edit, download, and permanently delete your contacts', 0, NULL),
(21, 'Google', 'https://www.google.com/m8/feeds', 'See, edit, download, and permanently delete your contacts', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/compute.readonly', 'View your Google Compute Engine resources', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/compute', 'View and manage your Google Compute Engine resources', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/cloudruntimeconfig', 'Manage your Google Cloud Platform services'' runtime configuration', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/cloudkms', 'View and manage your keys and secrets stored in Cloud Key Management Service', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/cloudiot', 'Register and manage devices in the Google Cloud IoT service', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/cloud_search.stats.indexing', 'Index and serve your organization''s data with Cloud Search', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/cloud_search.stats', 'Index and serve your organization''s data with Cloud Search', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/cloud_search.settings.query', 'Index and serve your organization''s data with Cloud Search', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/cloud_search.settings.indexing', 'Index and serve your organization''s data with Cloud Search', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/cloud_search.settings', 'Index and serve your organization''s data with Cloud Search', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/cloud_search.query', 'Search your organization''s data in the Cloud Search index', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/cloud_search.indexing', 'Index and serve your organization''s data with Cloud Search', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/cloud_search.debug', 'Index and serve your organization''s data with Cloud Search', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/cloud_search', 'Index and serve your organization''s data with Cloud Search', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/cloud_debugger', 'Use Stackdriver Debugger', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/cloud-vision', 'Apply machine learning models to understand and label images', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/cloud-translation', 'Translate text from one language to another using Google Translate', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/cloud-platform.read-only', 'View your data across Google Cloud Platform services', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/cloud-platform', 'View and manage your data across Google Cloud Platform services', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/cloud-language', 'Apply machine learning models to reveal the structure and meaning of text', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/cloud-identity.groups.readonly', 'See any Cloud Identity Groups that you can access, including group members and their emails', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/cloud-identity.groups', 'See, change, create, and delete any of the Cloud Identity Groups that you can access, including the members of each group', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/cloud-bigtable.admin.table', 'Administer your Cloud Bigtable tables', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/cloud-bigtable.admin.cluster', 'Administer your Cloud Bigtable clusters', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/cloud-bigtable.admin', 'Administer your Cloud Bigtable tables and clusters', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/classroom.topics.readonly', 'View topics in Google Classroom', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/classroom.topics', 'See, create, and edit topics in Google Classroom', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/classroom.student-submissions.students.readonly', 'View course work and grades for students in the Google Classroom classes you teach or administer', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/classroom.student-submissions.me.readonly', 'View your course work and grades in Google Classroom', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/classroom.rosters.readonly', 'View your Google Classroom class rosters', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/classroom.rosters', 'Manage your Google Classroom class rosters', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/classroom.push-notifications', 'Receive notifications about your Google Classroom data', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/classroom.profile.photos', 'View the profile photos of people in your classes', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/classroom.profile.emails', 'View the email addresses of people in your classes', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/classroom.guardianlinks.students.readonly', 'View guardians for students in your Google Classroom classes', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/classroom.guardianlinks.students', 'View and manage guardians for students in your Google Classroom classes', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/classroom.guardianlinks.me.readonly', 'View your Google Classroom guardians', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/classroom.coursework.students.readonly', 'View course work and grades for students in the Google Classroom classes you teach or administer', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/classroom.coursework.students', 'Manage course work and grades for students in the Google Classroom classes you teach and view the course work and grades for classes you administer', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/classroom.coursework.me.readonly', 'View your course work and grades in Google Classroom', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/classroom.coursework.me', 'Manage your course work and view your grades in Google Classroom', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/classroom.courses.readonly', 'View your Google Classroom classes', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/classroom.courses', 'Manage your Google Classroom classes', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/classroom.announcements.readonly', 'View announcements in Google Classroom', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/classroom.announcements', 'View and manage announcements in Google Classroom', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/calendar.settings.readonly', 'View your Calendar settings', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/calendar.readonly', 'View your calendars', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/calendar.events.readonly', 'View events on all your calendars', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/calendar.events', 'View and edit events on all your calendars', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/calendar', 'See, edit, share, and permanently delete all the calendars you can access using Google Calendar', 0, NULL),
(21, 'Google', 'https://www.google.com/calendar/feeds', 'See, edit, share, and permanently delete all the calendars you can access using Google Calendar', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/books', 'Manage your books', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/blogger.readonly', 'View your Blogger account', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/blogger', 'Manage your Blogger account', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/bigtable.admin.table', 'Administer your Cloud Bigtable tables', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/bigtable.admin.instance', 'Administer your Cloud Bigtable clusters', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/bigtable.admin.cluster', 'Administer your Cloud Bigtable clusters', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/bigtable.admin', 'Administer your Cloud Bigtable tables and clusters', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/bigquery.readonly', 'View your data in Google BigQuery', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/bigquery.insertdata', 'Insert data into Google BigQuery', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/bigquery', 'View and manage your data in Google BigQuery', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/apps.order.readonly', 'Manage users on your domain', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/apps.order', 'Manage users on your domain', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/apps.licensing', 'View and manage G Suite licenses for your domain', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/apps.groups.settings', 'View and manage the settings of a G Suite group', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/apps.groups.migration', 'Manage messages in groups on your domain', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/apps.alerts', 'See and delete your domain''s G Suite alerts, and send alert feedback', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/appengine.admin', 'View and manage your applications deployed on Google App Engine', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/androidpublisher', 'View and manage your Google Play Developer account', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/androidmanagement', 'Manage Android devices and apps for your customers', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/androidenterprise', 'Manage corporate Android devices', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/analytics.user.deletion', 'Manage Google Analytics user deletion requests', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/analytics.readonly', 'View your Google Analytics data', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/analytics.provision', 'Create a new Google Analytics account along with its default property and view', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/analytics.manage.users.readonly', 'View Google Analytics user permissions', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/analytics.manage.users', 'Manage Google Analytics Account users by email address', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/analytics.edit', 'Edit Google Analytics management entities', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/analytics', 'View and manage your Google Analytics data', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/adsensehost', 'View and manage your AdSense host data and associated accounts', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/adsense.readonly', 'View your AdSense data', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/adsense', 'View and manage your AdSense data', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.reports.usage.readonly', 'View usage reports for your G Suite domain', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.reports.audit.readonly', 'View audit reports for your G Suite domain', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.directory.userschema.readonly', 'View user schemas on your domain', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.directory.userschema', 'View and manage the provisioning of user schemas on your domain', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.directory.user.security', 'Manage data access permissions for users on your domain', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.directory.user.readonly', 'View users on your domain', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.directory.user.alias.readonly', 'View user aliases on your domain', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.directory.user.alias', 'View and manage user aliases on your domain', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.directory.user', 'View and manage the provisioning of users on your domain', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.directory.rolemanagement.readonly', 'View delegated admin roles for your domain', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.directory.rolemanagement', 'Manage delegated admin roles for your domain', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.directory.resource.calendar.readonly', 'View calendar resources on your domain', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.directory.resource.calendar', 'View and manage the provisioning of calendar resources on your domain', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.directory.orgunit.readonly', 'View organization units on your domain', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.directory.orgunit', 'View and manage organization units on your domain', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.directory.notifications', 'View and manage notifications received on your domain', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.directory.group.readonly', 'View groups on your domain', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.directory.group.member.readonly', 'View group subscriptions on your domain', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.directory.group.member', 'View and manage group subscriptions on your domain', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.directory.group', 'View and manage the provisioning of groups on your domain', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.directory.domain.readonly', 'View domains related to your customers', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.directory.domain', 'View and manage the provisioning of domains for your customers', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.directory.device.mobile.readonly', 'View your mobile devices'' metadata', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.directory.device.mobile.action', 'Manage your mobile devices by performing administrative tasks', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.directory.device.mobile', 'View and manage your mobile devices'' metadata', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.directory.device.chromeos.readonly', 'View your Chrome OS devices'' metadata', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.directory.device.chromeos', 'View and manage your Chrome OS devices'' metadata', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.directory.customer.readonly', 'View customer related information', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.directory.customer', 'View and manage customer related information', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.datatransfer.readonly', 'View data transfers between users in your organization', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/admin.datatransfer', 'View and manage data transfers between users in your organization', 0, NULL),
(21, 'Google', 'https://www.googleapis.com/auth/adexchange.buyer', 'Manage your Ad Exchange buyer account configuration', 0, NULL);

-- 22. 华为 (HuaweiScope - Deprecated)
INSERT INTO `oauth_provider_scope` (`provider_id`, `provider_name`, `scope`, `description`, `is_default`, `memo`) VALUES
(22, '华为', 'https://www.huawei.com/auth/account/base.profile', '获取用户的基本信息', 1, 'scope 含义，以 description 为准'),
(22, '华为', 'https://www.huawei.com/auth/account/mobile.number', '获取用户的手机号', 0, NULL),
(22, '华为', 'https://www.huawei.com/auth/account/accountlist', '获取用户的账单列表', 0, NULL),
(22, '华为', 'https://www.huawei.com/auth/drive.file', '只允许访问由应用程序创建或打开的文件', 0, '以下两个 scope 不需要经过华为评估和验证'),
(22, '华为', 'https://www.huawei.com/auth/drive.appdata', '只允许访问由应用程序创建或打开的文件', 0, NULL),
(22, '华为', 'https://www.huawei.com/auth/drive', '只允许访问由应用程序创建或打开的文件', 0, '使用前需要向drivekit@huawei.com提交申请'),
(22, '华为', 'https://www.huawei.com/auth/drive.readonly', '只允许访问由应用程序创建或打开的文件', 0, NULL),
(22, '华为', 'https://www.huawei.com/auth/drive.metadata', '只允许访问由应用程序创建或打开的文件', 0, NULL),
(22, '华为', 'https://www.huawei.com/auth/drive.metadata.readonly', '只允许访问由应用程序创建或打开的文件', 0, NULL);

-- 22. 华为 (HuaweiV3Scope)
INSERT INTO `oauth_provider_scope` (`provider_id`, `provider_name`, `scope`, `description`, `is_default`, `memo`) VALUES
(22, '华为', 'openid', '基础scope，v3必选', 1, 'scope 含义，以 description 为准'),
(22, '华为', 'https://www.huawei.com/auth/account/base.profile', '获取用户的基本信息', 1, 'scope 含义，以 description 为准'),
(22, '华为', 'https://www.huawei.com/auth/account/mobile.number', '获取用户的手机号', 0, NULL),
(22, '华为', 'https://www.huawei.com/auth/account/accountlist', '获取用户的账单列表', 0, NULL),
(22, '华为', 'https://www.huawei.com/auth/drive.file', '只允许访问由应用程序创建或打开的文件', 0, '以下两个 scope 不需要经过华为评估和验证'),
(22, '华为', 'https://www.huawei.com/auth/drive.appdata', '只允许访问由应用程序创建或打开的文件', 0, NULL),
(22, '华为', 'https://www.huawei.com/auth/drive', '只允许访问由应用程序创建或打开的文件', 0, '使用前需要向drivekit@huawei.com提交申请'),
(22, '华为', 'https://www.huawei.com/auth/drive.readonly', '只允许访问由应用程序创建或打开的文件', 0, NULL),
(22, '华为', 'https://www.huawei.com/auth/drive.metadata', '只允许访问由应用程序创建或打开的文件', 0, NULL),
(22, '华为', 'https://www.huawei.com/auth/drive.metadata.readonly', '只允许访问由应用程序创建或打开的文件', 0, NULL);

-- 23. 京东
INSERT INTO `oauth_provider_scope` (`provider_id`, `provider_name`, `scope`, `description`, `is_default`, `memo`) VALUES
(23, '京东', 'snsapi_base', '基础授权', 1, 'scope 含义，以 description 为准');

-- 24. Line
INSERT INTO `oauth_provider_scope` (`provider_id`, `provider_name`, `scope`, `description`, `is_default`, `memo`) VALUES
(24, 'Line', 'profile', 'Get profile details', 1, 'scope 含义，以 description 为准'),
(24, 'Line', 'openid', 'Get id token', 1, NULL),
(24, 'Line', 'email', 'Get email (separate authorization required)', 0, NULL);

-- 25. LinkedIn
INSERT INTO `oauth_provider_scope` (`provider_id`, `provider_name`, `scope`, `description`, `is_default`, `memo`) VALUES
(25, 'LinkedIn', 'r_liteprofile', 'Use your name, headline, and photo', 1, 'scope 含义，以 description 为准'),
(25, 'LinkedIn', 'r_emailaddress', 'Use the primary email address associated with your LinkedIn account', 1, NULL),
(25, 'LinkedIn', 'w_member_social', 'Post, comment and like posts on your behalf', 1, NULL),
(25, 'LinkedIn', 'r_member_social', 'Retrieve your posts, comments, likes, and other engagement data', 0, NULL),
(25, 'LinkedIn', 'r_ad_campaigns', 'View advertising campaigns you manage', 0, NULL),
(25, 'LinkedIn', 'r_ads', 'Retrieve your advertising accounts', 0, NULL),
(25, 'LinkedIn', 'r_ads_leadgen_automation', 'Access your Lead Gen Forms and retrieve leads', 0, NULL),
(25, 'LinkedIn', 'r_ads_reporting', 'Retrieve reporting for your advertising accounts', 0, NULL),
(25, 'LinkedIn', 'r_basicprofile', 'Use your basic profile including your name, photo, headline, and current positions', 0, NULL),
(25, 'LinkedIn', 'r_organization_social', 'Retrieve your organizations'' posts, including any comments, likes and other engagement data', 0, NULL),
(25, 'LinkedIn', 'rw_ad_campaigns', 'Manage your advertising campaigns', 0, NULL),
(25, 'LinkedIn', 'rw_ads', 'Manage your advertising accounts', 0, NULL),
(25, 'LinkedIn', 'rw_company_admin', 'For V1 callsManage your organization''s page and post updates', 0, NULL),
(25, 'LinkedIn', 'rw_dmp_segments', 'Create and manage your matched audiences', 0, NULL),
(25, 'LinkedIn', 'rw_organization_admin', 'Manage your organizations'' pages and retrieve reporting data', 0, NULL),
(25, 'LinkedIn', 'rw_organization', 'For V2 callsManage your organization''s page and post updates', 0, NULL),
(25, 'LinkedIn', 'w_organization_social', 'Post, comment and like posts on your organization''s behalf', 0, NULL),
(25, 'LinkedIn', 'w_share', 'Post updates to LinkedIn as you', 0, NULL);

-- 26. Microsoft
INSERT INTO `oauth_provider_scope` (`provider_id`, `provider_name`, `scope`, `description`, `is_default`, `memo`) VALUES
(26, 'Microsoft', 'profile', '允许应用查看用户的基本个人资料（名称、图片、用户名称）', 1, 'scope 含义，以 description 为准'),
(26, 'Microsoft', 'email', '允许应用读取用户的主电子邮件地址', 1, NULL),
(26, 'Microsoft', 'openid', '允许用户以其工作或学校帐户登录应用，并允许应用查看用户的基本个人资料信息', 1, NULL),
(26, 'Microsoft', 'offline_access', '允许应用读取和更新用户数据，即使用户当前没有在使用此应用，也不例外', 1, NULL),
(26, 'Microsoft', 'User.Read', '登录并读取用户个人资料', 0, NULL),
(26, 'Microsoft', 'User.ReadWrite', '对用户个人资料的读写权限', 0, NULL),
(26, 'Microsoft', 'User.ReadBasic.All', '读取所有用户的基本个人资料', 0, NULL),
(26, 'Microsoft', 'User.Read.All', '读取所有用户的完整个人资料', 0, NULL),
(26, 'Microsoft', 'User.ReadWrite.All', '读取和写入所有用户的完整个人资料', 0, NULL),
(26, 'Microsoft', 'User.Invite.All', '将来宾用户邀请到组织', 0, NULL),
(26, 'Microsoft', 'User.Export.All', '导出用户数据', 0, NULL),
(26, 'Microsoft', 'User.ManageIdentities.All', '管理所有用户标识', 0, NULL),
(26, 'Microsoft', 'UserActivity.ReadWrite.CreatedByApp', '将应用活动读取和写入到用户的活动源', 0, NULL),
(26, 'Microsoft', 'Files.Read', '允许应用读取登录用户的文件', 0, NULL),
(26, 'Microsoft', 'Files.Read.All', '允许应用读取登录用户可以访问的所有文件', 0, NULL),
(26, 'Microsoft', 'Files.ReadWrite', '允许应用读取、创建、更新和删除登录用户的文件', 0, NULL),
(26, 'Microsoft', 'Files.ReadWrite.All', '允许应用读取、创建、更新和删除登录用户可以访问的所有文件', 0, NULL),
(26, 'Microsoft', 'Files.ReadWrite.AppFolder', '允许应用读取、创建、更新和删除应用程序文件夹中的文件', 0, NULL),
(26, 'Microsoft', 'Files.Read.Selected', '允许应用读取用户选择的文件。在用户选择文件后，应用有几个小时的访问权限', 0, NULL),
(26, 'Microsoft', 'Files.ReadWrite.Selected', '允许应用读取和写入用户选择的文件。在用户选择文件后，应用有几个小时的访问权限', 0, NULL),
(26, 'Microsoft', 'OrgContact.Read.All', '允许应用代表已登录用户读取所有组织联系人。这些联系人由组织管理，不同于用户的个人联系人', 0, NULL),
(26, 'Microsoft', 'Mail.Read', '允许应用读取用户邮箱中的电子邮件', 0, NULL),
(26, 'Microsoft', 'Mail.ReadBasic', '允许应用读取已登录用户的邮箱，但不读取 body、bodyPreview、uniqueBody、attachments、extensions 和任何扩展属性。不包含邮件搜索权限', 0, NULL),
(26, 'Microsoft', 'Mail.ReadWrite', '允许应用创建、读取、更新和删除用户邮箱中的电子邮件。不包括发送电子邮件的权限', 0, NULL),
(26, 'Microsoft', 'Mail.Read.Shared', '允许应用读取用户可以访问的邮件，包括用户个人邮件和共享邮件', 0, NULL),
(26, 'Microsoft', 'Mail.ReadWrite.Shared', '允许应用创建、读取、更新和删除用户有权访问的邮件，包括用户个人邮件和共享邮件。不包括邮件发送权限', 0, NULL),
(26, 'Microsoft', 'Mail.Send', '允许应用以组织用户身份发送邮件', 0, NULL),
(26, 'Microsoft', 'Mail.Send.Shared', '允许应用以登录用户身份发送邮件，包括代表他人发送邮件', 0, NULL),
(26, 'Microsoft', 'MailboxSettings.Read', '允许应用读取用户的邮箱设置。不包括邮件发送权限', 0, NULL),
(26, 'Microsoft', 'MailboxSettings.ReadWrite', '允许应用创建、读取、更新和删除用户邮箱设置。不包含直接发送邮件的权限，但允许应用创建能够转发或重定向邮件的规则', 0, NULL),
(26, 'Microsoft', 'Notes.Read', '允许应用代表已登录用户读取 OneNote 笔记本和分区标题并创建新的页面、笔记本和分区', 0, NULL),
(26, 'Microsoft', 'Notes.Create', '允许应用代创建用户 OneNote 笔记本', 0, NULL),
(26, 'Microsoft', 'Notes.ReadWrite', '允许应用代表已登录用户读取、共享和修改 OneNote 笔记本', 0, NULL),
(26, 'Microsoft', 'Notes.Read.All', '允许应用读取登录用户在组织中有权访问的 OneNote 笔记本', 0, NULL),
(26, 'Microsoft', 'Notes.ReadWrite.All', '允许应用读取、共享和修改已登录用户在组织中有权访问的 OneNote 笔记本', 0, NULL);

-- 27. Pinterest
INSERT INTO `oauth_provider_scope` (`provider_id`, `provider_name`, `scope`, `description`, `is_default`, `memo`) VALUES
(27, 'Pinterest', 'read_public', 'Use GET method on a user''s Pins, boards.', 1, NULL),
(27, 'Pinterest', 'write_public', 'Use PATCH, POST and DELETE methods on a user''s Pins and boards.', 0, NULL),
(27, 'Pinterest', 'read_relationships', 'Use GET method on a user''s follows and followers (on boards, users and interests).', 0, NULL),
(27, 'Pinterest', 'write_relationships', 'Use PATCH, POST and DELETE methods on a user''s follows and followers (on boards, users and interests).', 0, NULL);

-- 28. QQ
INSERT INTO `oauth_provider_scope` (`provider_id`, `provider_name`, `scope`, `description`, `is_default`, `memo`) VALUES
(28, 'QQ', 'get_user_info', '获取登录用户的昵称、头像、性别', 1, NULL),
(28, 'QQ', 'get_vip_info', '获取QQ会员的基本信息', 0, '以下 scope 需要申请：http://wiki.connect.qq.com/openapi权限申请'),
(28, 'QQ', 'get_vip_rich_info', '获取QQ会员的高级信息', 0, NULL),
(28, 'QQ', 'list_album', '获取用户QQ空间相册列表', 0, NULL),
(28, 'QQ', 'upload_pic', '上传一张照片到QQ空间相册', 0, NULL),
(28, 'QQ', 'add_album', '在用户的空间相册里，创建一个新的个人相册', 0, NULL),
(28, 'QQ', 'list_photo', '获取用户QQ空间相册中的照片列表', 0, NULL);

-- 29. Slack
INSERT INTO `oauth_provider_scope` (`provider_id`, `provider_name`, `scope`, `description`, `is_default`, `memo`) VALUES
(29, 'Slack', 'users.profile:read', 'View profile details about people in a workspace', 1, NULL),
(29, 'Slack', 'users:read', 'View people in a workspace', 1, NULL),
(29, 'Slack', 'users:read.email', 'View email addresses of people in a workspace', 1, NULL),
(29, 'Slack', 'users.profile:write', 'Edit a user''s profile information and status', 0, NULL),
(29, 'Slack', 'users.profile:write:user', 'Change the user''s profile fields', 0, NULL),
(29, 'Slack', 'users:write', 'Set presence for your slack app', 0, NULL),
(29, 'Slack', 'admin', 'Administer a workspace', 0, NULL),
(29, 'Slack', 'admin.analytics:read', 'Access analytics data about the organization', 0, NULL),
(29, 'Slack', 'admin.apps:read', 'View apps and app requests in a workspace', 0, NULL),
(29, 'Slack', 'admin.apps:write', 'Manage apps in a workspace', 0, NULL),
(29, 'Slack', 'admin.barriers:read', 'Read information barriers in the organization', 0, NULL),
(29, 'Slack', 'admin.barriers:write', 'Manage information barriers in the organization', 0, NULL),
(29, 'Slack', 'admin.conversations:read', 'View the channel''s member list, topic, purpose and channel name', 0, NULL),
(29, 'Slack', 'admin.conversations:write', 'Start a new conversation, modify a conversation and modify channel details', 0, NULL),
(29, 'Slack', 'admin.invites:read', 'Gain information about invite requests in a Grid organization.', 0, NULL),
(29, 'Slack', 'admin.invites:write', 'Approve or deny invite requests in a Grid organization.', 0, NULL),
(29, 'Slack', 'admin.teams:read', 'Access information about a workspace', 0, NULL),
(29, 'Slack', 'admin.teams:write', 'Make changes to a workspace', 0, NULL),
(29, 'Slack', 'admin.usergroups:read', 'Access information about user groups', 0, NULL),
(29, 'Slack', 'admin.usergroups:write', 'Make changes to your usergroups', 0, NULL),
(29, 'Slack', 'admin.users:read', 'Access a workspace''s profile information', 0, NULL),
(29, 'Slack', 'admin.users:write', 'Modify account information', 0, NULL),
(29, 'Slack', 'app_mentions:read', 'View messages that directly mention @your_slack_app in conversations that the app is in', 0, NULL),
(29, 'Slack', 'auditlogs:read', 'View events from all workspaces, channels and users (Enterprise Grid only)', 0, NULL),
(29, 'Slack', 'bot', 'Add the ability for people to direct message or mention @your_slack_app', 0, NULL),
(29, 'Slack', 'calls:read', 'View information about ongoing and past calls', 0, NULL),
(29, 'Slack', 'calls:write', 'Start and manage calls in a workspace', 0, NULL),
(29, 'Slack', 'channels:history', 'View messages and other content in public channels that your slack app has been added to', 0, NULL),
(29, 'Slack', 'channels:join', 'Join public channels in a workspace', 0, NULL),
(29, 'Slack', 'channels:manage', 'Manage public channels that your slack app has been added to and create new ones', 0, NULL),
(29, 'Slack', 'channels:read', 'View basic information about public channels in a workspace', 0, NULL),
(29, 'Slack', 'channels:write', 'Manage a user''s public channels and create new ones on a user''s behalf', 0, NULL),
(29, 'Slack', 'chat:write', 'Post messages in approved channels & conversations', 0, NULL),
(29, 'Slack', 'chat:write.customize', 'Send messages as @your_slack_app with a customized username and avatar', 0, NULL),
(29, 'Slack', 'chat:write.public', 'Send messages to channels @your_slack_app isn''t a member of', 0, NULL),
(29, 'Slack', 'chat:write:bot', 'Send messages as your slack app', 0, NULL),
(29, 'Slack', 'chat:write:user', 'Send messages on a user''s behalf', 0, NULL),
(29, 'Slack', 'client', 'Receive all events from a workspace in real time', 0, NULL),
(29, 'Slack', 'commands', 'Add shortcuts and/or slash commands that people can use', 0, NULL),
(29, 'Slack', 'conversations:history', 'Deprecated: Retrieve conversation history for legacy workspace apps', 0, NULL),
(29, 'Slack', 'conversations:read', 'Deprecated: Retrieve information on conversations for legacy workspace apps', 0, NULL),
(29, 'Slack', 'conversations:write', 'Deprecated: Edit conversation attributes for legacy workspace apps', 0, NULL),
(29, 'Slack', 'dnd:read', 'View Do Not Disturb settings for people in a workspace', 0, NULL),
(29, 'Slack', 'dnd:write', 'Edit a user''s Do Not Disturb settings', 0, NULL),
(29, 'Slack', 'dnd:write:user', 'Change the user''s Do Not Disturb settings', 0, NULL),
(29, 'Slack', 'emoji:read', 'View custom emoji in a workspace', 0, NULL),
(29, 'Slack', 'files:read', 'View files shared in channels and conversations that your slack app has been added to', 0, NULL),
(29, 'Slack', 'files:write', 'Upload, edit, and delete files as your slack app', 0, NULL),
(29, 'Slack', 'files:write:user', 'Upload, edit, and delete files as your slack app', 0, NULL),
(29, 'Slack', 'groups:history', 'View messages and other content in private channels that your slack app has been added to', 0, NULL),
(29, 'Slack', 'groups:read', 'View basic information about private channels that your slack app has been added to', 0, NULL),
(29, 'Slack', 'groups:write', 'Manage private channels that your slack app has been added to and create new ones', 0, NULL),
(29, 'Slack', 'identify', 'View information about a user''s identity', 0, NULL),
(29, 'Slack', 'identity.avatar', 'View a user''s Slack avatar', 0, NULL),
(29, 'Slack', 'identity.avatar:read:user', 'View the user''s profile picture', 0, NULL),
(29, 'Slack', 'identity.basic', 'View information about a user''s identity', 0, NULL),
(29, 'Slack', 'identity.email', 'View a user''s email address', 0, NULL),
(29, 'Slack', 'identity.email:read:user', 'This scope is not yet described.', 0, NULL),
(29, 'Slack', 'identity.team', 'View a user''s Slack workspace name', 0, NULL),
(29, 'Slack', 'identity.team:read:user', 'View the workspace''s name, domain, and icon', 0, NULL),
(29, 'Slack', 'identity:read:user', 'This scope is not yet described.', 0, NULL),
(29, 'Slack', 'im:history', 'View messages and other content in direct messages that your slack app has been added to', 0, NULL),
(29, 'Slack', 'im:read', 'View basic information about direct messages that your slack app has been added to', 0, NULL),
(29, 'Slack', 'im:write', 'Start direct messages with people', 0, NULL),
(29, 'Slack', 'incoming-webhook', 'Create one-way webhooks to post messages to a specific channel', 0, NULL),
(29, 'Slack', 'links:read', 'View URLs in messages', 0, NULL),
(29, 'Slack', 'links:write', 'Show previews of URLs in messages', 0, NULL),
(29, 'Slack', 'mpim:history', 'View messages and other content in group direct messages that your slack app has been added to', 0, NULL),
(29, 'Slack', 'mpim:read', 'View basic information about group direct messages that your slack app has been added to', 0, NULL),
(29, 'Slack', 'mpim:write', 'Start group direct messages with people', 0, NULL),
(29, 'Slack', 'none', 'Execute methods without needing a scope', 0, NULL),
(29, 'Slack', 'pins:read', 'View pinned content in channels and conversations that your slack app has been added to', 0, NULL),
(29, 'Slack', 'pins:write', 'Add and remove pinned messages and files', 0, NULL),
(29, 'Slack', 'post', 'Post messages to a workspace', 0, NULL),
(29, 'Slack', 'reactions:read', 'View emoji reactions and their associated content in channels and conversations that your slack app has been added to', 0, NULL),
(29, 'Slack', 'reactions:write', 'Add and edit emoji reactions', 0, NULL),
(29, 'Slack', 'read', 'View all content in a workspace', 0, NULL),
(29, 'Slack', 'reminders:read', 'View reminders created by your slack app', 0, NULL),
(29, 'Slack', 'reminders:read:user', 'Access reminders created by a user or for a user', 0, NULL),
(29, 'Slack', 'reminders:write', 'Add, remove, or mark reminders as complete', 0, NULL),
(29, 'Slack', 'reminders:write:user', 'Add, remove, or complete reminders for the user', 0, NULL),
(29, 'Slack', 'remote_files:read', 'View remote files added by the app in a workspace', 0, NULL),
(29, 'Slack', 'remote_files:share', 'Share remote files on a user''s behalf', 0, NULL),
(29, 'Slack', 'remote_files:write', 'Add, edit, and delete remote files on a user''s behalf', 0, NULL),
(29, 'Slack', 'search:read', 'Search a workspace''s content', 0, NULL),
(29, 'Slack', 'stars:read', 'View messages and files that your slack app has starred', 0, NULL),
(29, 'Slack', 'stars:write', 'Add or remove stars', 0, NULL),
(29, 'Slack', 'team:read', 'View the name, email domain, and icon for workspaces your slack app is connected to', 0, NULL),
(29, 'Slack', 'tokens.basic', 'Execute methods without needing a scope', 0, NULL),
(29, 'Slack', 'usergroups:read', 'View user groups in a workspace', 0, NULL),
(29, 'Slack', 'usergroups:write', 'Create and manage user groups', 0, NULL),
(29, 'Slack', 'workflow.steps:execute', 'Add steps that people can use in Workflow Builder', 0, NULL);

-- 30. Stack Overflow
INSERT INTO `oauth_provider_scope` (`provider_id`, `provider_name`, `scope`, `description`, `is_default`, `memo`) VALUES
(30, 'Stack Overflow', 'read_inbox', 'access a user''s global inbox', 1, NULL),
(30, 'Stack Overflow', 'no_expiry', 'access_token''s with this scope do not expire', 0, NULL),
(30, 'Stack Overflow', 'write_access', 'perform write operations as a user', 0, NULL),
(30, 'Stack Overflow', 'private_info', 'access full history of a user''s private actions on the site', 0, NULL);

-- 33. 微博
INSERT INTO `oauth_provider_scope` (`provider_id`, `provider_name`, `scope`, `description`, `is_default`, `memo`) VALUES
(33, '微博', 'all', '获取所有权限', 1, NULL),
(33, '微博', 'email', '用户的联系邮箱', 0, NULL),
(33, '微博', 'direct_messages_write', '私信发送接口', 0, NULL),
(33, '微博', 'direct_messages_read', '私信读取接口', 0, NULL),
(33, '微博', 'invitation_write', '邀请发送接口', 0, NULL),
(33, '微博', 'friendships_groups_read', '好友分组读取接口组', 0, NULL),
(33, '微博', 'friendships_groups_write', '好友分组写入接口组', 0, NULL),
(33, '微博', 'statuses_to_me_read', '定向微博读取接口组', 0, NULL),
(33, '微博', 'follow_app_official_microblog', '关注应用官方微博，该参数不对应具体接口，只需在应用控制台填写官方帐号即可。', 0, NULL);

-- 37. 小米
INSERT INTO `oauth_provider_scope` (`provider_id`, `provider_name`, `scope`, `description`, `is_default`, `memo`) VALUES
(37, '小米', 'user/profile', '获取用户的基本信息', 1, NULL),
(37, '小米', 'user/openIdV2', '获取用户的OpenID', 1, NULL),
(37, '小米', 'user/phoneAndEmail', '获取用户的手机号和邮箱', 1, NULL);