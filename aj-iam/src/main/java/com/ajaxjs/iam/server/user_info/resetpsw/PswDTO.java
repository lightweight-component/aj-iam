package com.ajaxjs.iam.server.user_info.resetpsw;

import lombok.Data;

@Data
public class PswDTO {
    String oldPsw;
    String newPsw;
}
