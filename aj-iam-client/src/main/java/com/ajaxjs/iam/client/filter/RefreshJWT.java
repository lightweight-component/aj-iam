package com.ajaxjs.iam.client.filter;

import com.ajaxjs.iam.UserConstants;
import com.ajaxjs.iam.client.BaseOidcClientUserController;
import com.ajaxjs.iam.client.model.TokenValidDetail;
import com.ajaxjs.iam.jwt.JwtAccessToken;
import com.ajaxjs.util.JsonUtil;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.WebUtils;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 刷新访问令牌
 */
@AllArgsConstructor
public class RefreshJWT {
    TokenValidDetail tokenValidDetail;

    HttpServletRequest request;

    HttpServletResponse response;

    UserInterceptor userInterceptor;

    /**
     * 从 HTTP 请求中提取刷新令牌
     *
     * @param request HTTP 请求对象
     * @return 提取到的刷新令牌，如果未找到则返回 null
     */
    public static String extractRefreshToken(HttpServletRequest request) {
        String token = request.getHeader(UserConstants.REFRESH_TOKEN_KEY);

        // 如果从请求头的"Authorization"字段提取不到 token，尝试从请求头的"token"字段提取
        if (token == null) {
            token = WebUtils.getCookie(request, UserConstants.REFRESH_TOKEN_KEY);
            if (token == null)
                token = request.getParameter(UserConstants.REFRESH_TOKEN_KEY);
        }

        return StrUtil.isEmptyText(token) ? null : token;
    }

    /**
     * 刷新用户访问令牌
     *
     * @param refreshToken 用于刷新访问令牌的刷新令牌字符串
     */
    @SuppressWarnings("unchecked")
    public void refreshToken(String refreshToken) {
        Map<String, Object> result = userInterceptor.refreshToken(refreshToken);  // 调用拦截器刷新令牌接口

        if ((int) result.get("status") == 1) {
            // 将返回数据转换为JwtAccessToken对象并设置到Cookie中
            JwtAccessToken token = JsonUtil.map2pojo((Map<String, Object>) result.get("data"), JwtAccessToken.class);
            BaseOidcClientUserController.setTokenToCookie(token, response);
        } else
            System.err.println("刷新失败:" + result.get("message"));
    }

    private static final long REFRESH_THRESHOLD = 60 * 1000; // 60秒内过期就刷新

    /**
     * 检查 Access Token 是否快过期了，且有有效的 Refresh Token，则刷新
     */
    public void checkAlmostExpire() {
        long timeUntilExpiry = tokenValidDetail.getExpiredTime() - System.currentTimeMillis() / 1000;

        if (timeUntilExpiry < REFRESH_THRESHOLD) {
            String refreshToken = RefreshJWT.extractRefreshToken(request);

            if (refreshToken != null) {
                // 刷新成功，生成新的 Access Token
                refreshToken(refreshToken);
            }
        }
    }

    /**
     * 检查并刷新过期的访问令牌
     *
     * @return boolean 刷新成功返回true，否则返回false
     */
    public boolean expiredRefresh() {
        String refreshToken = RefreshJWT.extractRefreshToken(request);

        if (refreshToken != null) {
            refreshToken(refreshToken);
            // 刷新成功，生成新的 Access Token
            return true;
        } else
            return false;
    }
}
