# Admin UI for AJ-IAM



```shell
npm run dev
npm run build
```

## 可配置列表与表单

`@ajax-js/configurable-widget@1.0.1` 提供 `ListMgr`、`FormMgr`、`ListLoader`、`FormLoader`、`ListInfo` 和 `FormInfo`。
管理页使用 `window.__AJ_DATASERVICE_CONFIG__.adminApiRoot`，加载器使用 `window.config.dsApiRoot`；本项目在 `index.html` 中将两者指向同一个 API 根路径。
`/list-info` 和 `/system/form-info`（别名 `/form-info`）使用新包的配置编辑页。
安装时的 `postinstall` 会给新包补上 IAM 数字状态兼容：`1` 转为 `true`，`0` 转为 `false`。升级组件库后请重新核对这段兼容逻辑。
安装依赖前请备份本机的 `node_modules/@ajaxjs/ui` 和 `node_modules/@ajaxjs/util`；本项目仍有页面依赖它们，npm 可能会清理这些手动恢复的包。

线上管理端部署在 `https://wyndme.cn/admin/`，并与 `/iam_api/`、`/iam_login/` 同源。
管理端从 `/iam_api/client/to_login` 发起授权，IAM 后端通过
`/iam_api/client/callback` 接收回调并调用 `/iam_api/user_login/token` 兑换令牌。后端配置
`auth.clientId=lKi9p9FyicBd6eA`，并通过服务端配置提供对应的 `auth.clientSecret`。
本地 Vite 将 `/iam_api/`、`/iam_admin_api/` 和 `/iam_login/` 转发到线上站点。
在 `http://localhost:5173/` 登录后返回本地首页；开发环境的 API 操作也会作用于线上数据。

## config in Nginx


```nginx
server {
    listen       80;
    server_name  local.iam.com;


    location / { # 前端 UI
        proxy_pass http://localhost:5173/; # 代理到后端 API
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        # --- 以下配置是为了解决 WebSocket 代理问题 ---
        # 必须设置，否则 WebSocket 无法升级
        # proxy_cookie_domain localhost local.robot.com;
        proxy_http_version 1.1;
        # 传递 Upgrade 和 Connection 头，这是 WebSocket 握手的关键
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
    }   
    
    location /iam_admin_api/ {
        proxy_pass http://localhost:8082/iam_api/; # 代理到后端 API
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_set_header admin-protection on; # 添加 admin 权限
    }
}


location /admin/ {
    alias /home/static-html/aj-iam-admin/;
    index index.html;
}

location /iam_admin_api/ {
    proxy_pass http://localhost:8082/iam_api/; # 代理到后端 API
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
    proxy_set_header admin-protection on; # 添加 admin 权限
}
```
