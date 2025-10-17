package com.ajaxjs.iam.user.service;


import com.ajaxjs.framework.model.BaseEntityConstants;
import com.ajaxjs.framework.model.BusinessException;
import com.ajaxjs.iam.UserConstants;
import com.ajaxjs.iam.client.SecurityManager;
import com.ajaxjs.iam.model.SimpleUser;
import com.ajaxjs.iam.server.model.po.App;
import com.ajaxjs.iam.server.service.OAuthCommon;
import com.ajaxjs.iam.user.controller.UserController;
import com.ajaxjs.iam.user.model.User;
import com.ajaxjs.spring.DiContextUtil;
import com.ajaxjs.sqlman.Sql;
import com.ajaxjs.sqlman.crud.Entity;
import com.ajaxjs.sqlman.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.function.Function;

@Service
public class UserService implements UserController, UserConstants {
    @Autowired
    @Qualifier("passwordEncode")
    Function<String, String> passwordEncode;

    @Override
    public User currentUserInfo() {
        Long userId = SecurityManager.getUser().getId();

        return info(userId);
    }

    @Override
    public User info(Long id) {
        String sql = "SELECT u.*, t.name AS tenantName FROM user u LEFT JOIN tenant t ON u.tenant_id = t.id WHERE u.stat != 1 AND u.id = ?";
//        sql = TenantService.addTenantIdQuery(sql);

        return Sql.instance().input(sql, id).query(User.class);
    }

    @Override
    public User queryUserByClient(String authorization, String field, String value) {
        App app = OAuthCommon.getAppByAuthHeader(authorization);
        String sql = "SELECT * FROM user WHERE stat != 1 AND tenant_id = ? AND " + Utils.escapeSqlInjection(field) + " = ?";

        return Sql.instance().input(sql, app.getTenantId(), value).query(User.class);
    }

    /**
     * 在 Request 上下文中获取 User 对象
     *
     * @return User 对象
     */
    public static SimpleUser getUserFromRequestCxt() {
        return getUserFromRequestCxt(Objects.requireNonNull(DiContextUtil.getRequest()));
    }

    /**
     * 在 Request 上下文中获取 User 对象
     *
     * @param req 请求对象
     * @return User 对象
     */
    public static SimpleUser getUserFromRequestCxt(HttpServletRequest req) {
        return (SimpleUser) req.getAttribute(UserConstants.USER_KEY_IN_REQUEST);
    }

    @Override
    public User info() {
        SimpleUser user = getUserFromRequestCxt();

        return info(user.getId());
    }

    @Override
    public Boolean updateBySession(User user) {
        return null;
    }

    @Override
    public Boolean update(User user) {
        return Entity.instance().setTableName("user").input(user).update().isOk();
    }

    @Override
    public Boolean delete(Long id) {
        User user = new User();
        user.setId(id);
        user.setStat(BaseEntityConstants.STATUS_DELETED);  // 逻辑删除

        return update(user);
    }

    public static User getUserById(Long id) {
        User user = Sql.instance().input("SELECT * FROM user WHERE stat != 1 AND id = ?", id).query(User.class);

        if (user == null)
            throw new BusinessException("The user with id#" + id + " does not exist");

        return user;
    }
}
