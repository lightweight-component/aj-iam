package com.ajaxjs.iam;

import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.WebUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Enumeration;

@Slf4j
public abstract class BaseUserInterceptor {
    /**
     * 向客户端返回消息和 HTTP 状态码。
     *
     * @param resp        用于向客户端发送响应的 HttpServletResponse 对象。
     * @param httpErrCode 要返回的 HTTP 错误状态码。
     * @param msg         要返回给客户端的消息。
     */
    protected static void returnMsg(HttpServletResponse resp, int httpErrCode, String msg) {
        resp.setStatus(httpErrCode);// 设置 HTTP 响应状态码
        resp.setCharacterEncoding("UTF-8"); // 设置响应的字符编码和内容类型
        resp.setContentType("application/json;charset=utf-8");

        try (PrintWriter writer = resp.getWriter()) {// 使用 PrintWriter 对象将消息写入响应体
            writer.write(msg);
        } catch (IOException e) {
            log.warn("err::", e);// 捕获并记录写入响应过程中可能出现的 IO 异常
        }
    }

    /**
     * 获取 expiresIn 与当前时间对比，看是否超时
     *
     * @param expiresIn 超时
     * @return true 表示超时
     */
    static boolean checkIfExpire(long expiresIn) {
        LocalDateTime expiresDateTime = LocalDateTime.ofEpochSecond(expiresIn, 0, ZoneOffset.UTC);// 过期日期

        return expiresDateTime.isBefore(LocalDateTime.now());
    }

    /**
     * 从 HTTP 请求中提取 token。
     * 首先尝试从请求头的"Authorization"字段提取 token，如果不存在，则从"authorization"字段尝试提取。
     * 若以上两种方式都未能提取到 token，则从请求头的"token"字段和请求参数的"access_token"字段尝试提取。
     * 如果最终都未能提取到 token，将记录警告日志。
     *
     * @param request HttpServletRequest 对象，代表一个 HTTP 请求。
     * @return 返回提取到的 token，如果未能提取到则返回 null。
     */
    public static String extractToken(HttpServletRequest request) {
//        String token = extractHeaderToken(request); // 尝试从请求头的"Authorization"字段提取 token
        String token = request.getHeader("Authorization"); // 尝试从请求头的"Authorization"字段以另一种大小写形式提取 token

        // 如果从请求头的"Authorization"字段提取不到 token，尝试从请求头的"token"字段提取
        if (token == null) {
            token = request.getHeader("token");

            if (token == null) {

                token = WebUtils.getCookie(request, UserConstants.ACCESS_TOKEN_KEY);
                // 如果从请求头的"token"字段提取不到token，尝试从请求参数的"access_token"字段提取
                if (token == null) {
                    token = request.getParameter(UserConstants.ACCESS_TOKEN_KEY);

                    // 如果上述方式都提取不到 token，记录警告日志
                    if (token == null)
                        log.warn("Token not found in request parameters. Not an OAuth2 request. path: " + request.getRequestURI());
                }
            }
        }

        if (StrUtil.isEmptyText(token))
            return null;

        if (token.toLowerCase().startsWith(BEARER_TYPE))
            token = token.substring(7).trim();

        return token;
    }

    private static final String BEARER_TYPE = "bearer";

    /**
     * 从 HTTP 请求中提取认证 Token。
     * 该方法从请求的 Authorization 头中寻找 Bearer 类型的 Token 值。
     *
     * @param request HttpServletRequest 对象，代表一个 HTTP 请求。
     * @return 返回提取到的 Token 字符串，如果找不到合适的 Token 则返回 null。
     */
    private static String extractHeaderToken(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders("Authorization"); // 获取所有名为"Authorization"的请求头
        String value;

        do {
            if (!headers.hasMoreElements()) return null;   // 如果没有更多的元素，则表示没有找到合适的认证信息，返回 null
            value = headers.nextElement();// 获取下一个头元素的值
        } while (!value.toLowerCase().startsWith(BEARER_TYPE.toLowerCase())); // 忽略大小写，查找以"Bearer "开始的值

        String authHeaderValue = value.substring(BEARER_TYPE.length()).trim();  // 从"Bearer "开始提取Token值，并去除前后空格

        // 如果 Token 值中包含逗号，则截取逗号前的部分，防止解析错误
        int commaIndex = authHeaderValue.indexOf(44); // 44 为逗号的 ASCII 码
        if (commaIndex > 0) authHeaderValue = authHeaderValue.substring(0, commaIndex);

        return authHeaderValue;
    }
}
