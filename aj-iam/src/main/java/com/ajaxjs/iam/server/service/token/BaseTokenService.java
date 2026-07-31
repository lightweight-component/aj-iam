package com.ajaxjs.iam.server.service.token;

import com.ajaxjs.iam.model.App;
import com.ajaxjs.iam.server.model.User;
import com.ajaxjs.spring.DiContextUtil;
import com.ajaxjs.util.ObjectHelper;
import lombok.Data;

import java.util.Calendar;
import java.util.Date;

/**
 * Base service for token operations
 */
@Data
public abstract class BaseTokenService {
    private App app;

    private String grantType;

    private User user;

    /**
     * Token 的有效期，单位：分钟，默认两天
     */
    private Integer tokenExpires = 60 * 24 * 2;

    /**
     * Refresh Token 的有效期，单位：分钟，默认7天
     */
    private Integer refreshTokenExpires = 60 * 24 * 7;

    public Integer getTokenExpires() {
        String s = DiContextUtil.getConfigFromYml("auth.token.expires");

        return ObjectHelper.hasText(s) ? Integer.parseInt(s) : this.tokenExpires;
    }

    /**
     * 创建 BaseTokenService
     *
     * @param app       应用程序对象，表示当前授权的应用
     * @param grantType 授权类型，指定令牌的授权方式
     * @param user      用户
     */
    public BaseTokenService(App app, String grantType, User user) {
        this(app, grantType);
        this.user = user;
    }

    /**
     * 创建 BaseTokenService
     *
     * @param app       应用程序对象，表示当前授权的应用
     * @param grantType 授权类型，指定令牌的授权方式
     */
    public BaseTokenService(App app, String grantType) {
        this.app = app;
        this.grantType = grantType;
    }

    /**
     * 将到期的分钟数转换为到期的时间
     */
    static Date calculateExpirationDate(int secondsToExpiration) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, secondsToExpiration);

        return calendar.getTime();
    }
}
