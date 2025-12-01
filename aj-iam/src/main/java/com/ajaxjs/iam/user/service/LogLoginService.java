package com.ajaxjs.iam.user.service;

import com.ajaxjs.iam.UserConstants;
import com.ajaxjs.iam.user.controller.LogLoginController;
import com.ajaxjs.iam.user.model.LogLogin;
import com.ajaxjs.iam.user.model.User;
import com.ajaxjs.security.iplist.IpList;
import com.ajaxjs.sqlman.Action;
import com.ajaxjs.sqlman.crud.page.PageResult;
import com.ajaxjs.sqlman.model.CreateResult;
import com.ajaxjs.util.httpremote.Get;
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

        if (TenantService.getTenantId() != null)
            userLoginLog.setTenantId(TenantService.getTenantId());

        CreateResult<Long> result = new Action(userLoginLog).create().execute(true, Long.class);

        if (!result.isOk())
            log.warn("更新会员登录日志出错");
    }

    void saveIp(LogLogin bean, HttpServletRequest req) {
        if (req == null)
            return;

        String ip = IpList.getClientIp(req);

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
