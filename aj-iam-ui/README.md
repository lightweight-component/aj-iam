# Admin UI for AJ-IAM



```shell
npm run dev
npm run build
```

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