package com.ajaxjs.iam.client;

import com.ajaxjs.iam.UserConstants;
import com.ajaxjs.iam.model.SimpleUser;
import com.ajaxjs.util.Version;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class SecurityManager {
    /**
     * 获取当前请求的 HttpServletRequest 对象
     * 如果当前没有请求上下文，则根据是否正在运行测试来返回对应的请求对象
     *
     * @return 当前请求的 HttpServletRequest 对象，如果不存在请求上下文则返回 null
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        assert requestAttributes != null;
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    /**
     * Get the user from context
     *
     * @return SimpleUser
     */
    public static SimpleUser getUser() {
        SimpleUser user = null;
        HttpServletRequest request = getRequest();
        Object obj = request.getAttribute(UserConstants.USER_KEY_IN_REQUEST);

        if (obj != null)
            user = (SimpleUser) obj;

        if (user == null && Version.isDebug) {
            user = new SimpleUser();
            user.setName("Jack");
            user.setId(888L);
            user.setTenantId(8);
        }

        return user;
    }
}
