package com.ajaxjs.iam.server.service;

import com.ajaxjs.framework.database.DataBaseConnection;
import com.ajaxjs.iam.UserConstants;
import com.ajaxjs.iam.server.controller.LogLoginController;
import com.ajaxjs.iam.server.model.LogLogin;
import com.ajaxjs.iam.server.model.User;
import com.ajaxjs.security.iplist.IpList;
import com.ajaxjs.sqlman.Action;
import com.ajaxjs.sqlman.JdbcConnection;
import com.ajaxjs.sqlman.page.PageResult;
import com.ajaxjs.sqlman.model.CreateResult;
import com.ajaxjs.util.httpremote.Get;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class LogLoginService implements LogLoginController, UserConstants {
    @Autowired
    @Qualifier("loginLogExecutor")
    private ThreadPoolTaskExecutor executor;

    /**
     * 用户登录日志
     */
    public void saveLoginLog(User user, HttpServletRequest req) {
        try {
            LogLogin userLoginLog = new LogLogin();
            userLoginLog.setUserId(user.getId());
            userLoginLog.setLoginType(LoginType.PASSWORD);
            userLoginLog.setUserName(user.getLoginId());
            if (user.getTenantId() != null)
                userLoginLog.setTenantId(user.getTenantId().intValue());

            // 请求结束后不能再访问 req，先在登录线程中提取需要的数据。
            if (req != null) {
                userLoginLog.setIp(IpList.getClientIp(req));
                userLoginLog.setUserAgent(req.getHeader("user-agent"));
            }

            executor.execute(() -> saveLoginLogAsync(userLoginLog));
        } catch (Exception e) {
            log.warn("提交会员登录日志任务出错", e);
        }
    }

    void saveLoginLogAsync(LogLogin bean) {
        try {
            fillIpLocation(bean);

            // 在线程内独立获取连接，避免查询地区时占用数据库连接。
            try {
                if (DataBaseConnection.initDb() == null)
                    throw new IllegalStateException("获取登录日志数据库连接失败");

                CreateResult<Long> result = new Action(bean).create().execute(true, Long.class);
                if (!result.isOk())
                    log.warn("保存会员登录日志出错");
            } finally {
                JdbcConnection.closeConnection();
            }
        } catch (Exception e) {
            log.warn("保存会员登录日志出错", e);
        }
    }

    void fillIpLocation(LogLogin bean) {
        String ip = bean.getIp();
        if (ip == null || ip.isEmpty())
            return;

        if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "localhost";
            bean.setIpLocation("本机");
        } else {
//            这个接口太慢
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
