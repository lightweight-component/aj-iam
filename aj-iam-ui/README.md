# Admin UI for AJ-IAM



```shell
npm run dev
npm run build
```

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
