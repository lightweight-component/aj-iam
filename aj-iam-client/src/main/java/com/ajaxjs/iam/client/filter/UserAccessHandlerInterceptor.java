package com.ajaxjs.iam.client.filter;

import com.ajaxjs.iam.annotation.NeedsUserLogined;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

/**
 * 检查用户是否已登录的拦截器
 */
@Deprecated
public class UserAccessHandlerInterceptor implements HandlerInterceptor {
    public static final String USER_IN_SESSION = "USER";

    /**
     * 在处理请求之前进行拦截，用于检查用户是否已登录。
     * 如果处理程序方法上注解了 @NeedsUserLogined，表示该方法需要用户登录后才能访问。
     * 如果用户未登录，将返回 401 未授权的响应。
     *
     * @param request  HTTP 请求对象
     * @param response HTTP 响应对象
     * @param handler  将要处理请求的处理器对象
     * @return 如果用户已登录，返回 true，继续处理请求；否则返回 false，终止请求处理
     * @throws Exception 如果处理过程中发生异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {  // 检查处理器是否为 HandlerMethod 类型，即是否是一个方法处理请求

            Method method = ((HandlerMethod) handler).getMethod();// 获取处理请求的具体方法

            if (method.getAnnotation(NeedsUserLogined.class) != null) {// 检查方法上是否注解了 NeedsUserLogined，表示该方法需要用户登录
                HttpSession session = request.getSession();  // 获取会话对象，用于检查用户是否已登录
                boolean login = session.getAttribute(USER_IN_SESSION) != null;   // 判断用户是否已登录

                if (!login) {
                    // 用户未登录，返回401未授权的消息
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("text/plain; charset=UTF-8");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().println("未登录，请先进行登录");
                }

                return login; // 返回用户是否已登录的状态，true表示已登录，可以继续处理请求；false 表示未登录，终止请求处理
            }
        }

        return true;// 直接放行，继续处理请求
    }
}
