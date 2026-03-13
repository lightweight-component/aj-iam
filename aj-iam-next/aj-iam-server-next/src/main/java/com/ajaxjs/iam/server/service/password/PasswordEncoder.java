package com.ajaxjs.iam.server.service.password;

import com.ajaxjs.util.HashHelper;

/**
 * 密码加密规则
 */
public class PasswordEncoder {
    /**
     * 盐值
     */
    public String salt;

    /**
     * 基本的密码加密
     *
     * @param psw 明文
     * @return 密文
     */
    public String md5salt(String psw) {
        return HashHelper.md5(psw + salt).toLowerCase();
    }
}
