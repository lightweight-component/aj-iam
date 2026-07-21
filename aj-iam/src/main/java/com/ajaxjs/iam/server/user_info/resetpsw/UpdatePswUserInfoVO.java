package com.ajaxjs.iam.server.user_info.resetpsw;

import lombok.Data;

/**
 * The user info for updating password
 */
@Data
public class UpdatePswUserInfoVO {
    String password;
    Long id;
}
