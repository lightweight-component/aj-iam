package com.ajaxjs.iam.user.service;

/**
 * 用户业务功能
 */
public class UserFunction {
    /**
     * 是否验证了（手机号码、邮箱、实名、银行卡）的状态总值，采用 8421 码
     */
    interface VerifiedState {
        int PHONE = 1;
        int EMAIL = 2;
        int REAL_PERSON = 4;
        int BANK_CARD = 8;
    }

    /**
     * 绑定第三方登录账号的状态总值，采用 8421 码
     */
    public interface BindState {
        /**
         * 本用户系统
         */
        int IAM = 1;

        int WECHAT = 2;
    }
}
