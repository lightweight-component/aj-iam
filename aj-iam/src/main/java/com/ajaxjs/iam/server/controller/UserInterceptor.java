package com.ajaxjs.iam.server.controller;


import com.ajaxjs.iam.BaseUserInterceptor;
import com.ajaxjs.iam.UserConstants;
import com.ajaxjs.iam.annotation.AllowAccess;
import com.ajaxjs.iam.jwt.JWebToken;
import com.ajaxjs.iam.jwt.JWebTokenMgr;
import com.ajaxjs.iam.model.SimpleUser;
import com.ajaxjs.util.JsonUtil;
import com.ajaxjs.util.Version;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 资源拦截器
 * 用法：
 * <code>
 *
 * @Bean UserInterceptor authInterceptor() {
 * return new UserInterceptor();
 * }
 * </code>
 */
@Slf4j
public class UserInterceptor extends BaseUserInterceptor implements HandlerInterceptor {
    @Value("${auth.run:true}")
    private String run;

    @Value("${auth.cacheType:jwt}")
    private String cacheType;

    @Autowired(required = false)
    private StringRedisTemplate redis;

    /**
     * 本地可以获取用户信息
     * 这个回调函数决定如何获取
     */
    @Autowired(required = false)
    @Qualifier("getuserfromjvmhash")
    private Function<String, String> getUserFromJvmHash;

    /**
     * JWT 验证的密钥
     */
    @Value("${User.oidc.jwtSecretKey:Df87sD#$%#A}")
    private String jwtSecretKey;

    /**
     * JWT 解密
     */
    @Bean
    JWebTokenMgr jWebTokenMgr() {
        JWebTokenMgr mgr = new JWebTokenMgr();
        mgr.setSecretKey(jwtSecretKey);

        return mgr;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            Method method = ((HandlerMethod) handler).getMethod();

            if (method.isAnnotationPresent(AllowAccess.class))
                return true;
        }

        if (Version.isDebug && "1".equals(request.getParameter("allow"))) // 方便开发
            return true;
        else if (StringUtils.hasText(run) && Boolean.parseBoolean(run)) {
            String token = extractToken(request);

            if (!StringUtils.hasText(token))
                return returnErrorMsg(401, response);

            String jsonUser;

            switch (cacheType) {
                case "redis":
                    jsonUser = redis.opsForValue().get(UserConstants.REDIS_PREFIX + token);
                    break;
                case "jvm_hash":
                    if (getUserFromJvmHash == null) {
                        serverErr(response, "配置参数 jvm_hash 不正确");

                        return false;
                    } else
                        jsonUser = getUserFromJvmHash.apply(token);
                    break;
                case "jwt":
                    JWebTokenMgr mgr = jWebTokenMgr();
                    JWebToken jwt = mgr.parse(token);

                    if (mgr.isValid(jwt)) {
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
}
