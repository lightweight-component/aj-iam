package com.ajaxjs.oauth.impl.stackoverflow;

import com.ajaxjs.oauth.AuthScope;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StackOverflowScope implements AuthScope {
    read_inbox("read_inbox", "access a user's global inbox", true),
    NO_EXPIRY("no_expiry", "access_token's with this scope do not expire", false),
    WRITE_ACCESS("write_access", "perform write operations as a user", false),
    PRIVATE_INFO("private_info", "access full history of a user's private actions on the site", false);

    private final String scope;
    private final String description;
    private final boolean isDefault;

}
