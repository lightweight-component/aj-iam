package com.ajaxjs.iam.server.service;

import com.ajaxjs.framework.fileupload.DetectType;
import com.ajaxjs.framework.fileupload.FileUploadAction;
import com.ajaxjs.framework.fileupload.UploadUtils;
import com.ajaxjs.framework.fileupload.UploadedResult;
import com.ajaxjs.framework.fileupload.policy.StorageType;
import com.ajaxjs.framework.model.BaseEntityConstants;
import com.ajaxjs.framework.model.BusinessException;
import com.ajaxjs.iam.UserConstants;
import com.ajaxjs.iam.client.SecurityManager;
import com.ajaxjs.iam.model.SimpleUser;
import com.ajaxjs.iam.server.model.po.App;
import com.ajaxjs.iam.server.service.OAuthCommon;
import com.ajaxjs.iam.server.controller.UserController;
import com.ajaxjs.iam.server.model.User;
import com.ajaxjs.spring.DiContextUtil;
import com.ajaxjs.sqlman.util.Utils;
import com.ajaxjs.sqlman.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UncheckedIOException;
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

        return new Action(sql).query(id).one(User.class);
    }

    @Override
    public User queryUserByClient(String authorization, String field, String value) {
        App app = OAuthCommon.getAppByAuthHeader(authorization);
        String sql = "SELECT * FROM user WHERE stat != 1 AND tenant_id = ? AND " + Utils.escapeSqlInjection(field) + " = ?";

        return new Action(sql).query(app.getTenantId(), value).one(User.class);
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
        Long userId = SecurityManager.getUser().getId();
        user.setId(userId);

        return new Action(user, "user").update().withId().isOk();
    }

    @Override
    public Boolean delete(Long id) {
        User user = new User();
        user.setId(id);
        user.setStat(BaseEntityConstants.STATUS_DELETED);  // 逻辑删除

        return update(user);
    }

    @Override
    @FileUploadAction(storageType = StorageType.DATABASE, detectType = DetectType.IMAGE, maxFileSize = 3)
    public UploadedResult avatar(MultipartFile file) {
        Long userId = SecurityManager.getUser().getId();

        return UploadUtils.doUpload(getClass(), "avatar", file, null, (_file, config) -> {
            try {
                if (!new Action("UPDATE user SET avatar_blob = ? WHERE id = ?").update(_file.getBytes(), userId).execute().isOk())
                    throw new BusinessException("更新用户头像失败");
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }

            String filename = file.getOriginalFilename();
            UploadedResult result = new UploadedResult();
            result.setFileName(filename);
            result.setOriginalFileName(filename);
            result.setFileSize(file.getSize());

            return result;
        });
    }

    public static User getUserById(Long id) {
        User user = new Action("SELECT * FROM user WHERE stat != 1 AND id = ?").query(id).one(User.class);

        if (user == null)
            throw new BusinessException("The user with id#" + id + " does not exist");

        return user;
    }
}
