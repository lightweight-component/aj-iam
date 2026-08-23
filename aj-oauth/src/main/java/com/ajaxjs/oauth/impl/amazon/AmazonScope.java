package com.ajaxjs.oauth.impl.amazon;

import com.ajaxjs.oauth.AuthScope;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AmazonScope implements AuthScope {
    R_LITEPROFILE("profile", "The profile scope includes a user's name and email address", true),
    R_EMAILADDRESS("profile:user_id", "The profile:user_id scope only includes the user_id field of the profile", true),
    W_MEMBER_SOCIAL("postal_code", "This includes the user's zip/postal code number from their primary shipping address", true);

    private final String scope;
    private final String description;
    private final boolean isDefault;
}
