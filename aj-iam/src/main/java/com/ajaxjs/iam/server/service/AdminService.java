package com.ajaxjs.iam.server.service;

import com.ajaxjs.framework.mvc.unifiedreturn.BizAction;
import com.ajaxjs.iam.server.controller.AdminController;
import com.ajaxjs.iam.user.service.TenantService;
import com.ajaxjs.sqlman.Sql;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AdminService implements AdminController {
    @Override
    @BizAction("欢迎页")
    public Map<String, Object> welcome() {
        Integer tenantId = TenantService.getTenantId();
        return Sql.instance().input("SELECT\n" +
                "        (SELECT COUNT(id) FROM user_login_log WHERE tenant_id = ?) AS loginTimes,\n" +
                "        (SELECT COUNT(id) FROM user WHERE stat != 1 AND tenant_id = ?) AS userNum,\n" +
                "        (SELECT COUNT(id) FROM app WHERE stat != 1) AS clientNum,\n" +
                "        (SELECT COUNT(id) FROM access_token WHERE tenant_id = ?) AS onlineNum,\n" +
                "        (SELECT COUNT(id) FROM tenant WHERE stat != 1) AS tenantNum", tenantId, tenantId, tenantId).query();
    }
}
