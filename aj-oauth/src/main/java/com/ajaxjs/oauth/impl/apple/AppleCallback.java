package com.ajaxjs.oauth.impl.apple;

import com.ajaxjs.oauth.model.Callback;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class AppleCallback extends Callback {
    /**
     * 苹果仅在用户首次授权应用程序时返回此值。如果您的应用程序已经获得了用户的授权，那么苹果将不会再次返回此值
     *
     * @see <a href="https://developer.apple.com/documentation/sign_in_with_apple/useri">user info</a>
     */
    private String user;

    /**
     * 苹果错误信息，仅在用户取消授权时返回此值
     *
     * @see <a href="https://developer.apple.com/documentation/sign_in_with_apple/sign_in_with_apple_js/incorporating_sign_in_with_apple_into_other_platforms">error response</a>
     */
    private String error;
}
