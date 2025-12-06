package com.ajaxjs.iam.server.service;

import com.ajaxjs.iam.server.model.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.File;

/**
 * 用户头像
 */
@Data
@RequiredArgsConstructor
public class UserAvatar {
    /**
     * 是否使用简单存储
     * true=把头像图片存储在数据库之中，这样比较简单
     */
    private boolean simpleStore;

    private final User user;

    /**
     * 头像文件存储的前缀
     */
    private String filePrefix;

    public String getAvatar() {
        if (simpleStore) {
            String avatar = user.getAvatar();

            if (avatar.startsWith("http"))
                return avatar;
            else { // from file
                return filePrefix + File.separator + user.getAvatar();
            }
        } else
            return user.getAvatarBlob();
    }
}
