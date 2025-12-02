package com.ajaxjs.iam;

/**
 * 用户常量
 */
public interface UserConstants {
    String TOKEN = "token";

    String AUTHORIZATION = "Authorization";

    String ACCESS_TOKEN_KEY = "access_token";
    String REFRESH_TOKEN_KEY = "refresh_token";

    String REDIS_PREFIX = "USER:";

    String USER_KEY_IN_REQUEST = "USER_KEY_IN_REQUEST";

    /**
     * 性别
     */
    enum Gender {
        MALE, FEMALE, UNKNOWN
    }

    interface LoginIdType {
        int PSW_LOGIN_ID = 1;
        int PSW_LOGIN_EMAIL = 2;
        int PSW_LOGIN_PHONE = 4;
    }

    /**
     * 登录类型
     */
    interface LoginType {
        /**
         * 普通密码账号
         */
        int PASSWORD = 1;

        /**
         * 微信
         */
        int WECHAT = 2;

        /**
         * 微信小程序
         */
        int WECHAT_APPLET = 3;
    }
}
