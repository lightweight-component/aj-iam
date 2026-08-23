package com.ajaxjs.oauth.impl.apple;

import com.ajaxjs.oauth.model.Config;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class AppleConfig extends Config {

    /**
     * 苹果开发者账号中的密钥标识符
     *
     * @see <a href="https://developer.apple.com/help/account/configure-app-capabilities/create-a-sign-in-with-apple-private-key/">create-a-sign-in-with-apple-private-key</a>
     */
    private String kid;

    /**
     * 苹果开发者账号中的团队ID
     *
     * @see <a href="https://developer.apple.com/help/glossary/team-id/">team id</a>
     */
    private String teamId;
}
