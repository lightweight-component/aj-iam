package com.ajaxjs.iam.client;

import jakarta.servlet.http.HttpServletRequest;

public interface IOAuthService {
    boolean clientCredentialCheck(HttpServletRequest request);
}
