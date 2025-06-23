package com.ajaxjs.iam.user.service;


import com.ajaxjs.iam.user.common.UserConstants;
import com.ajaxjs.iam.user.controller.LogLoginController;
import com.ajaxjs.iam.user.model.LogLogin;
import com.ajaxjs.iam.user.model.User;
import com.ajaxjs.sqlman.crud.Entity;
import com.ajaxjs.sqlman.model.CreateResult;
import com.ajaxjs.sqlman.model.PageResult;
import com.ajaxjs.util.http_request.Get;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class LogLoginService implements LogLoginController, UserConstants {
    /**
     * 用户登录日志
     */
    public void saveLoginLog(User user, HttpServletRequest req) {
        LogLogin userLoginLog = new LogLogin();
        userLoginLog.setUserId(user.getId());
        userLoginLog.setLoginType(LoginType.PASSWORD);
        userLoginLog.setUserName(user.getLoginId());
        saveIp(userLoginLog, req);

        CreateResult<Long> result = Entity.newInstance().input(userLoginLog).create(Long.class);

        if (!result.isOk())
            log.warn("更新会员登录日志出错");
    }

    void saveIp(LogLogin bean, HttpServletRequest req) {
        if (req == null)
            return;

        String ip = getClientIp(req);

        if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "localhost";
            bean.setIpLocation("本机");
        } else {
            try {
                // ip 查询
                Map<String, Object> map = Get.api("http://ip-api.com/json/" + ip + "?lang=zh-CN");

                if (!map.containsKey("errMsg")) {
                    String location = map.get("country") + " " + map.get("regionName");
                    bean.setIpLocation(location);
                } else
                    throw new Exception("接口返回不成功 " + map.get("errMsg"));
            } catch (Exception e) {
                log.warn("saveIp wrong.", e);
            }
        }

        bean.setIp(ip);
        bean.setUserAgent(req.getHeader("user-agent"));
    }

    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多级代理会有逗号, 取第一个
            ip = ip.split(",")[0].trim();
        } else {
            ip = request.getHeader("Proxy-Client-IP");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                }
            }
        }

        return ip;
    }

    @Override
    public PageResult<LogLogin> page() {
        return null;
    }

    @Override
    public List<LogLogin> findListByUserId(Long userId) {
        return null;
    }

    @Override
    public LogLogin getLastUserLoginInfo(long userId) {
        return null;
    }
}
