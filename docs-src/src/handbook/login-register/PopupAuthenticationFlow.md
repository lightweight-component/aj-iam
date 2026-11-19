请为当前 Vue 3 + View UI Plus（iView）+ TypeScript 项目整合歪觅 IAM 统一登录。

目标：
使用远端登录界面 + pop-up 认证，实现登录、获取 JWT、保存用户信息和登录后跳转。不要编写本地账号密码/短信登录表单，不要通过本项目后端转发认证请求。

先检查当前项目的登录路由、Token 存储、用户信息存储、请求认证方式、登录后跳转和退出登录逻辑，再按现有约定接入。直接完成实现与验证。

一、服务地址

默认配置如下，可使用 Vite 环境变量覆盖：

VITE_IAM_API=https://live.wyndme.cn/iam_api
VITE_ROBOT_API=https://live.wyndme.cn/robot_api_v2

对应页面及接口：

- 登录状态检查：
  ${IAM_API}/wyndme_login/login-check.html
- 短信认证弹窗：
  ${IAM_API}/wyndme_login/login.html
- 密码认证弹窗：
  ${IAM_API}/wyndme_login/password-login.html
- 远端登录界面及 Token 桥接页面：
  ${ROBOT_API}/client/lowcode_to_login
- 获取当前用户：
  ${IAM_API}/curr_user/jsonp?access_token=JWT&callback=回调函数名

这是现有 IAM 页面提供的 postMessage 桥接协议。按该协议接入，不要自行改写成另一套 OIDC 流程。

二、登录流程

1. 在 /login 页面提供“登录歪觅账号”按钮。
2. 用户点击后，通过 window.open 打开登录状态检查页面。
3. 接收 LOGIN_CHECK_RESULT：
    - loggedIn=true：将已有弹窗导航到 Token 桥接页面，等待 JWT_RESULT。
    - loggedIn=false：关闭检查弹窗，在页面模态层中展示 iframe，src 为 Token 桥接页面，使用其中的远端登录界面。
4. 远端 iframe 发出 DO_LOGIN 后，根据短信或密码模式打开认证弹窗。
5. 认证弹窗返回 LOGIN_RESULT：
    - 成功：将已有认证弹窗导航到登录状态检查页面，再继续获取 JWT。
    - 失败：关闭弹窗，显示可重试的错误提示。
6. Token 桥接弹窗返回 JWT_RESULT 后，使用 JWT 调用当前用户 JSONP 接口。
7. 校验用户数据，按当前项目约定保存 Token、用户信息和登录状态。
8. 关闭弹窗及 iframe 模态层，执行当前项目原有的登录后跳转和用户状态更新。

检查登录状态和获取 JWT 时尽量复用已有弹窗，避免在异步 message 回调中反复新开窗口而被浏览器拦截。

三、postMessage 消息协议

1. 登录检查页面 → 主窗口：

{ type: "LOGIN_CHECK_RESULT", loggedIn: boolean }

2. 远端登录 iframe → 主窗口，短信模式：

{
type: "DO_LOGIN",
mode?: "sms",
phone?: string,
code?: string,
captchaCode?: string,
captchaUuid?: string
}

收到后，将以下字段放入 URLSearchParams，打开短信认证弹窗：

${IAM_API}/wyndme_login/login.html
?phone=...
&code=...
&captchaCode=...
&captchaUuid=...

保持现有接口协议，不输出或记录验证码。

3. 远端登录 iframe → 主窗口，密码模式：

{
type: "DO_LOGIN",
mode: "password",
username: string,
password: string,
captchaCode: string,
captchaUuid: string
}

打开：

${IAM_API}/wyndme_login/password-login.html?hostOrigin=当前应用origin

不要把账号、密码放入弹窗 URL。

等待密码认证弹窗发出：

{ type: "PASSWORD_LOGIN_READY" }

然后向该弹窗发送：

{
type: "PASSWORD_LOGIN_SUBMIT",
mode: "password",
username: string,
password: string,
captchaCode: string,
captchaUuid: string
}

postMessage 的 targetOrigin 必须是 IAM_API 的 origin，不使用 "*"。
发送后立即清除主窗口暂存的密码凭证。

4. 认证弹窗 → 主窗口：

{ type: "LOGIN_RESULT", loggedIn: boolean }

5. Token 桥接弹窗 → 主窗口：

{ type: "JWT_RESULT", jwt: string }

四、消息校验

必须同时检查 event.origin、event.source 和消息数据结构。

- LOGIN_CHECK_RESULT：
  origin 必须为 IAM_API 的 origin；
  source 必须为当前检查弹窗。
- PASSWORD_LOGIN_READY、LOGIN_RESULT：
  origin 必须为 IAM_API 的 origin；
  source 必须为当前认证弹窗。
- DO_LOGIN：
  origin 必须为 ROBOT_API 的 origin；
  source 必须为远端登录 iframe.contentWindow。
- JWT_RESULT：
  origin 必须为 ROBOT_API 的 origin；
  source 必须为当前 Token 桥接弹窗。

即使默认 IAM_API 和 ROBOT_API 同域，也要分别计算 origin。

消息数据校验：

- loggedIn 必须为 boolean。
- jwt 必须为非空 string。
- 密码模式的 username、password、captchaCode、captchaUuid 必须为非空 string。
- 短信模式各可选字段存在时必须为 string。
- 拒绝未知消息类型、来源不匹配的消息。
- 防止重复 JWT_RESULT 导致重复保存或跳转。

五、用户信息及会话接入

当前用户接口使用 JSONP：

- 动态创建 script 和唯一 callback 名。
- 超时建议 10 秒。
- 成功、失败、超时后均清理 script、回调和计时器。
- 校验返回用户至少包含 id、loginId。
- 保留完整用户信息，供现有头像、昵称和个人中心使用。

优先复用当前项目已有的 Token 和用户状态管理，不要另建一套不关联的登录状态。

如果项目没有既有存储约定，可以使用：

- localStorage.access_token：JWT
- localStorage.userInfo：完整用户对象的 JSON
- localStorage.isLoggedIn："true"

只有成功获得有效用户信息后才写入登录成功状态。
获取用户失败时应允许重新登录，不留下不完整会话。

按当前项目请求层约定使用 Token，不要仅保存 Token 却让业务请求无法携带认证信息。
退出登录时清理新增的会话数据。

登录后沿用当前项目的路由跳转逻辑。
参考实现使用 /?login_ok=1 通知布局刷新用户信息，但其他项目应使用自己的既有机制。

六、生命周期和交互

- 使用 Vue 3 Composition API 和 TypeScript。
- 主页面只提供统一登录入口、加载状态和错误提示。
- 账号、密码、验证码输入全部由远端 iframe 登录页面负责。
- iframe 模态层应有关闭按钮和合适的移动端尺寸。
- window.open 返回 null 时，提示允许本站弹窗。
- 检测用户手动关闭认证弹窗，恢复可重试状态。
- 登录流程应有超时处理，例如 3 分钟。
- 失败、关闭、超时或组件卸载时清理暂存凭证。
- 组件卸载时移除 message 监听、计时器并关闭所管理的弹窗。
- 异步获取用户信息结束后，如果组件已经卸载，不再保存会话或跳转。
- 直接访问 /login 时，不应再弹出“尚未登录，是否跳转登录”的重复提示。

七、验证与交付

完成生产构建和适用的类型检查，说明已有错误与本次新增错误。
使用模拟测试验证：

- IAM 已登录时获取 JWT 并完成跳转。
- IAM 未登录时展示远端 iframe。
- 短信登录流程。
- 密码弹窗 READY/SUBMIT 握手，密码不进入 URL。
- 拒绝错误 origin/source 的消息。
- 弹窗被拦截、关闭、超时及用户信息获取失败。
- 卸载清理和重复消息处理。

模拟测试不能替代真实 IAM 联调，不要声称未执行过的真实登录已验证。

最后说明修改文件、存储和跳转接入方式、配置项、验证结果及尚需联调的事项。