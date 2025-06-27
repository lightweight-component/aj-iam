package com.ajaxjs.iam.client;

import com.ajaxjs.iam.UserConstants;
import com.ajaxjs.iam.model.SimpleUser;
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

        if (user == null && isDebug) {
            user = new SimpleUser();
            user.setName("Jack");
            user.setId(888L);
            user.setTenantId(8);
        }

        return user;
    }

    public static boolean isDebug;

    /**
     * 获取操作系统名称
     */
    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();

    static {
        /*
         * 有两种模式：本地模式和远程模式（自动判断） 返回 true 表示是非 linux 环境，为开发调试的环境，即 isDebug = true； 返回
         * false 表示在部署的 linux 环境下。 Linux 的为远程模式
         */
        isDebug = !(OS_NAME.contains("nix") || OS_NAME.contains("nux") || OS_NAME.indexOf("aix") > 0);

    }
}
