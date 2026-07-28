package com.ajaxjs.iam.server.user_info;

import com.ajaxjs.framework.model.BaseEntityConstants;
import com.ajaxjs.framework.model.BusinessException;
import com.ajaxjs.iam.model.App;
import com.ajaxjs.iam.server.model.User;
import com.ajaxjs.iam.server.service.ClientCredential;
import com.ajaxjs.iam.server.user_info.controller.UserInfoController;
import com.ajaxjs.sqlman.Action;
import com.ajaxjs.sqlman.util.Utils;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService implements UserInfoController {
    /**
     * Get user information by user id.
     *
     * @param id User Id
     * @return User info.
     */
    public static User getUserInfoById(Long id) {
        String sql = "SELECT u.*, t.name AS tenantName FROM user u LEFT JOIN tenant t ON u.tenant_id = t.id WHERE u.stat != 1 AND u.id = ?";

        return new Action(sql).query(id).one(User.class);
    }

    @Override
    public User info(Long id) {
        return getUserInfoById(id);
    }

    @Override
    public User getUserInfoByAnyField(String field, String value) {
        App app = ClientCredential.getApp(ClientCredential.getAppId());
        String sql = "SELECT * FROM user WHERE stat != 1 AND tenant_id = ? AND " + Utils.escapeSqlInjection(field) + " = ?";

        return new Action(sql).query(app.getTenantId(), value).one(User.class);
    }

    @Override
    public boolean update(User user) {
        return new Action(user, "user").update().withId().isOk();
    }

    @Override
    public boolean delete(Long id) {
        String sql = "UPDATE user_account SET stat = 1 WHERE user_id = ?";
        new Action(sql).update(id).execute();

        User user = new User();
        user.setId(id);
        user.setStat(BaseEntityConstants.STATUS_DELETED);  // 逻辑删除

        return update(user);
    }

    @Override
    public Long getTotalUserNumberByTenant() {
        App app = ClientCredential.getApp(ClientCredential.getAppId());

        return new Action("SELECT COUNT(id) FROM user WHERE stat = 0 AND tenant_id = ?").query(app.getTenantId()).oneValue(Long.class);
    }

    public static User getUserByIdSimple(Long id) {
        User user = new Action("SELECT * FROM user WHERE stat != 1 AND id = ?").query(id).one(User.class);

        if (user == null)
            throw new BusinessException("The user with id#" + id + " does not exist");

        return user;
    }
}
