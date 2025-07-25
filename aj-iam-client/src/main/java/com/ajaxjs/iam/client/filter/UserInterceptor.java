package com.ajaxjs.iam.client.filter;

import com.ajaxjs.iam.UserConstants;
import com.ajaxjs.iam.annotation.AllowAccess;
import com.ajaxjs.iam.jwt.JWebToken;
import com.ajaxjs.iam.jwt.JWebTokenMgr;
import com.ajaxjs.iam.model.SimpleUser;
import com.ajaxjs.util.JsonUtil;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.Version;
import com.ajaxjs.util.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Enumeration;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 资源拦截器
 */
@Slf4j
public class UserInterceptor implements HandlerInterceptor {
    @Value("${auth.run:true}")
    private boolean run;

    @Value("${auth.cacheType:jwt}")
    private String cacheType;

//    @Autowired(required = false)
//    private StringRedisTemplate redis;

    /**
     * 本地可以获取用户信息
     * 这个回调函数决定如何获取
     */
    @Autowired(required = false)
    @Qualifier("getuserfromjvmhash")
    private Function<String, String> getUserFromJvmHash;

    @Autowired(required = false)
    JWebTokenMgr jWebTokenMgr;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            Method method = ((HandlerMethod) handler).getMethod();

            if (method.isAnnotationPresent(AllowAccess.class))
                return true;
        }

        if (Version.isDebug && "1".equals(request.getParameter("allow"))) // 方便开发
            return true;
        else if (run) {
            String token = extractToken(request);

            if (!StringUtils.hasText(token))
                return returnErrorMsg(401, response);

            String jsonUser;

            switch (cacheType) {
//                case "redis":
//                    jsonUser = redis.opsForValue().get(UserConstants.REDIS_PREFIX + token);
//                    break;
                case "jvm_hash":
                    if (getUserFromJvmHash == null) {
                        serverErr(response, "配置参数 jvm_hash 不正确");

                        return false;
                    } else
                        jsonUser = getUserFromJvmHash.apply(token);
                    break;
                case "jwt":
//                    JWebTokenMgr mgr = getBean(request, JWebTokenMgr.class);

                    JWebToken jwt = jWebTokenMgr.parse(token);

                    if (jWebTokenMgr.isValid(jwt)) {
                        jsonUser = "{\"id\": %s, \"name\": \"%s\", \"tenantId\":%s}";

                        Integer tenantId = null;
                        String aud = jwt.getPayload().getAud();

                        if (StringUtils.hasText(aud)) {
                            Matcher matcher = Pattern.compile("tenantId=(\\d+)").matcher(aud);

                            if (matcher.find())
                                tenantId = Integer.parseInt(matcher.group(1));
                        }

                        jsonUser = String.format(jsonUser, jwt.getPayload().getSub(), jwt.getPayload().getName(), tenantId);
                    } else {
                        returnErrorMsg(403, response);

                        return false;
                    }
                    break;
                default:
                    serverErr(response, "配置参数不正确");

                    return false;
            }

            if (StringUtils.hasText(jsonUser)) {
                log.debug(jsonUser);
                log.debug(new SimpleUser().toString());
                SimpleUser user = JsonUtil.fromJson(jsonUser, SimpleUser.class);
                request.setAttribute(UserConstants.USER_KEY_IN_REQUEST, user);

                return true;
            } else
                return returnErrorMsg(401, response);
        } else
            return true; // 关掉了认证
    }

    /**
     * 根据错误代码返回响应的信息
     *
     * @param status   错误代码
     * @param response 响应请求
     */
    private boolean returnErrorMsg(int status, HttpServletResponse response) {
        switch (status) {
            case 401:
                returnMsg(response, HttpStatus.UNAUTHORIZED.value(), "unauthorized", "未认证");
                break;
            case 403:
                returnMsg(response, HttpStatus.FORBIDDEN.value(), "forbidden", "没有权限");
                break;
            case 500:
                returnMsg(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), "error", "认证失败");
                break;
        }

        return false;
    }

    private final static String ERR_JSON = "{\"error\":\"%s\",\"error_description\":\"%s\"}";

    /**
     * 向客户端返回服务器错误信息。
     *
     * @param response 用于向客户端发送响应的 HttpServletResponse 对象。
     * @param msg      要返回给客户端的错误信息。
     */
    private void serverErr(HttpServletResponse response, String msg) {
        returnMsg(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), "error", msg);
    }

    /**
     * 返回响应信息
     *
     * @param resp        响应请求
     * @param httpErrCode 错误代码
     * @param title       错误标题
     * @param message     错误信息
     */
    private void returnMsg(HttpServletResponse resp, int httpErrCode, String title, String message) {
        returnMsg(resp, httpErrCode, String.format(ERR_JSON, title, message));
    }

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
