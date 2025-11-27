package com.ajaxjs.iam.client;

import com.ajaxjs.util.ObjectHelper;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ClientUtils {
    public static final String OAUTH_STATE = "OAUTH_STATE";

    // 返回自定义错误页面
    private static final String html = "<html><body><h1>403 Forbidden</h1><p>非法操作，请求被拒绝。</p></body></html>";

    /**
     * 返回 HTTP 403
     *
     * @param response 响应对象
     */
    public static void returnForbidden(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("text/html;charset=UTF-8");// 设置响应内容类型为HTML

        try {
            response.getWriter().println(html);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the annotation from the method or its interface
     *
     * @param handlerMethod   The method on the Interface
     * @param annotationClass The class of annotation
     * @param <T>             The type of annotation
     * @return The annotation
     */
    public static <T extends Annotation> T getAnnotationFromMethod(HandlerMethod handlerMethod, Class<T> annotationClass) {
        T annotation = handlerMethod.getMethodAnnotation(annotationClass);

        if (annotation != null)
            return annotation;

        Method method = handlerMethod.getMethod(); // The real controller method

        annotation = method.getAnnotation(annotationClass);

        if (annotation != null)
            return annotation;

        Class<?> controllerClass = handlerMethod.getBeanType();// 获取控制器类（方法所在的类）

        return controllerClass.getAnnotation(annotationClass);
    }

    /**
     * Get the annotation from: the method or its interface, the class or its interface
     *
     * @param handlerMethod   The method on the Interface
     * @param annotationClass The class of annotation
     * @param <T>             The type of annotation
     * @return The annotation
     */
    public static <T extends Annotation> T getAnnotationFromMethodAndClz(HandlerMethod handlerMethod, Class<T> annotationClass) {
        T annotationFromMethod = getAnnotationFromMethod(handlerMethod, annotationClass);
        if (annotationFromMethod != null)
            return annotationFromMethod;

        Class<?> controllerClass = handlerMethod.getBeanType();

        return AnnotatedElementUtils.findMergedAnnotation(controllerClass, annotationClass);
    }

    /**
     * 尝试从 Cookie 中提取指定名称的 value
     *
     * @param request    The request object
     * @param cookieName The name of cookie
     * @return The value of the cookie
     */
    public static String getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();

        if (!ObjectHelper.isEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName()))
                    return cookie.getValue();
            }
        }

        return null;
    }
}
