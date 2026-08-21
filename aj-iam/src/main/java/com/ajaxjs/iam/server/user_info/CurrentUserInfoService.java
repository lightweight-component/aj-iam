package com.ajaxjs.iam.server.user_info;

import com.ajaxjs.fileupload.DetectType;
import com.ajaxjs.fileupload.FileUploadAction;
import com.ajaxjs.fileupload.UploadUtils;
import com.ajaxjs.fileupload.UploadedResult;
import com.ajaxjs.fileupload.policy.StorageType;
import com.ajaxjs.framework.model.BaseEntityConstants;
import com.ajaxjs.framework.model.BusinessException;
import com.ajaxjs.iam.UserConstants;
import com.ajaxjs.iam.client.SecurityManager;
import com.ajaxjs.iam.model.SimpleUser;
import com.ajaxjs.iam.server.model.User;
import com.ajaxjs.iam.server.model.UserAccount;
import com.ajaxjs.iam.server.user_info.controller.CurrentUserInfoController;
import com.ajaxjs.spring.DiContextUtil;
import com.ajaxjs.sqlman.Action;
import com.ajaxjs.util.JsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Objects;

@Service
public class CurrentUserInfoService implements CurrentUserInfoController {
    @Override
    public User currentUserInfo() {
        Long userId = SecurityManager.getUser().getId();

        return UserInfoService.getUserInfoById(userId);

    }

    @Override
    public String currentUserInfoJSONP(String callback) {
        User user = currentUserInfo();
        String json = JsonUtil.toJson(user);

        return callback + "(" + json + ");";
    }

    @Override
    public boolean update(User user) {
        Long userId = SecurityManager.getUser().getId();
        user.setId(userId);

        return new Action(user, "user").update().withId().isOk();
    }

    @Override
    public boolean delete() {
        Long userId = SecurityManager.getUser().getId();
        String sql = "UPDATE user_account SET stat = 1 WHERE user_id = ?";
        new Action(sql).update(userId).execute();

        User user = new User();
        user.setId(userId);
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

    @Override
    public boolean setPasswordAtFirstTime(String password) {
        Long userId = SecurityManager.getUser().getId();

        String checkIfNotSetPsw = "SELECT password FROM user_account WHERE user_id = ? AND type = 'PASSWORD' AND stat = 0";
        String psw = new Action(checkIfNotSetPsw).query(userId).oneValue(String.class);

        if (psw != null)
            throw new BusinessException("用户已设置密码");

        // TODO
        String sql = "UPDATE user_account SET password = ? WHERE user_id = ? AND type = 'PASSWORD' AND stat = 0";
        new Action(sql).update(password, userId).execute();

        return true;
    }

    @Override
    public List<UserAccount> getUserAccountInfo() {
        Long userId = SecurityManager.getUser().getId();

        return new Action("SELECT id, identifier, login_type, type, create_date FROM user_account WHERE stat = 0 AND user_id = ?").query(userId).list(UserAccount.class);
    }

    /**
     * 在 Request 上下文中获取当前 User 对象
     *
     * @return User 对象，请注意该对象字段比较少
     */
    public static SimpleUser getUserFromRequestCxt() {
        return getUserFromRequestCxt(Objects.requireNonNull(DiContextUtil.getRequest()));
    }

    /**
     * 在 Request 上下文中获取当前 User 对象
     *
     * @param req 请求对象
     * @return User 对象，请注意该对象字段比较少
     */
    public static SimpleUser getUserFromRequestCxt(HttpServletRequest req) {
        return (SimpleUser) req.getAttribute(UserConstants.USER_KEY_IN_REQUEST);
    }
}
