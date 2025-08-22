package com.ajaxjs.iam.client.filter;

import com.ajaxjs.iam.UserConstants;
import com.ajaxjs.iam.client.model.TokenValidDetail;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.WebUtils;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@AllArgsConstructor
public class RefreshJWT {
    TokenValidDetail tokenValidDetail;

    HttpServletRequest request;

    HttpServletResponse response;

    UserInterceptor userInterceptor;

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

    public boolean refreshToken(String refreshToken) {
        Map<String, Object> result = userInterceptor.refreshToken(refreshToken);

    }

    private static final long REFRESH_THRESHOLD = 60 * 1000; // 60秒内过期就刷新

    /**
     * 检查 Access Token 是否快过期了，且有有效的 Refresh Token，则刷新
     */
    public void checkAlmostExpire() {
        long timeUntilExpiry = tokenValidDetail.getExpiredTime() - System.currentTimeMillis();

        if (timeUntilExpiry < REFRESH_THRESHOLD) {
            String refreshToken = RefreshJWT.extractRefreshToken(request);

            if (refreshToken != null) {
                // 刷新成功，生成新的 Access Token
                refreshToken(refreshToken);
            }
        }
    }

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
