```yaml
location /iam/ {
    ssi on;
    alias D:/sp42/code/ajaxjs/aj-iam/aj-iam-admin/;  # 注意路径斜杠
    index index.html index.htm; # 默认首页
    try_files $uri $uri/ /index.html; # 支持单页应用（SPA）路由

}

location /iam_api/ {
    proxy_pass http://localhost:8082/; # 代理到后端 API
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
}
```