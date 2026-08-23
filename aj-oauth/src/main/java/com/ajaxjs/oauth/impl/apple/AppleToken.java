package com.ajaxjs.oauth.impl.apple;

import com.ajaxjs.oauth.model.Token;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * Apple 附带属性
 */
@Data
@SuperBuilder
public class AppleToken extends Token {
    private String username;
}
