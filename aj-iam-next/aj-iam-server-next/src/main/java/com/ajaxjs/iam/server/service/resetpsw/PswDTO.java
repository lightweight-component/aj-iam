package com.ajaxjs.iam.server.service.resetpsw;

import lombok.Data;

@Data
public class PswDTO {
    String oldPsw;
    String newPsw;
}
