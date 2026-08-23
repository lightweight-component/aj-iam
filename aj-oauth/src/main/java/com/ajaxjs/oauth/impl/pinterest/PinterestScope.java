package com.ajaxjs.oauth.impl.pinterest;

import com.ajaxjs.oauth.AuthScope;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PinterestScope implements AuthScope {
    READ_PUBLIC("read_public", "Use GET method on a user’s Pins, boards.", true),
    WRITE_PUBLIC("write_public", "Use PATCH, POST and DELETE methods on a user’s Pins and boards.", false),
    READ_RELATIONSHIPS("read_relationships", "Use GET method on a user’s follows and followers (on boards, users and interests).", false),
    WRITE_RELATIONSHIPS("write_relationships", "Use PATCH, POST and DELETE methods on a user’s follows and followers (on boards, users and interests).", false),
    ;

    private final String scope;
    private final String description;
    private final boolean isDefault;
}
