package com.ajaxjs.iam.server.service;

import com.ajaxjs.framework.database.DataBaseConnection;
import com.ajaxjs.iam.UserConstants;
import com.ajaxjs.iam.client.IOAuthService;
import com.ajaxjs.iam.model.App;
import com.ajaxjs.iam.oauth.OAuthTools;
import com.ajaxjs.spring.DiContextUtil;
import com.ajaxjs.sqlman.Action;
import com.ajaxjs.util.Base64Utils;
import com.ajaxjs.util.CommonConstant;
import com.ajaxjs.util.ObjectHelper;
import com.ajaxjs.util.RandomTools;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * OAuth 2.0 中的客户端凭证（Client Credentials）授权模式请求
 */
@Service
public class ClientCredential implements IOAuthService {
    public boolean clientRegister(App app) {
        if (!StringUtils.hasText(app.getName()))
            throw new IllegalArgumentException("客户端的名称和回调地址不能为空");

        String clientId = RandomTools.generateRandomString(24);// 生成24位随机的 clientId
        App savedClientDetails = ClientCredential.getApp(clientId);

        // 生成的 clientId 必须是唯一的，尝试十次避免有重复的 clientId
        for (int i = 0; i < 10; i++) {
            clientId = RandomTools.generateRandomString(24);
            savedClientDetails = ClientCredential.getApp(clientId);
        }

        app.setClientId(clientId);
        app.setClientSecret(RandomTools.generateRandomString(32));

        // 保存到数据库
        return new Action(app, "app").create().execute(true).isOk();
    }

    @Override
    public boolean clientCredentialCheck(HttpServletRequest request) {
        String authorization = request.getHeader(UserConstants.AUTHORIZATION);

        if (ObjectHelper.isEmptyText(authorization))
            throw new IllegalArgumentException("Illegal arguments of client authorization");

        try (Connection ignored = DataBaseConnection.initDb()) {
            App app = getAppByAuthHeader(authorization);
            request.setAttribute(CLIENT_ID, app.getClientId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    public static final String CLIENT_ID = "CLIENT_ID";

    /**
     * 在 Request 上下文中获取应用 ID
     *
     * @return 应用 ID
     */
    public static String getAppId() {
        if (DiContextUtil.getRequest() == null)
            return "";

        Object obj = DiContextUtil.getRequest().getAttribute(CLIENT_ID);

        if (obj == null)
            throw new NullPointerException("CLIENT_ID is null, not initialized");

        return (String) obj;
    }

    /**
     * 根据 HTTP 头的 authorization 获取 App 信息
     */
    public static App getAppByAuthHeader(String authorization) {
        authorization = authorization.replaceAll("Basic ", CommonConstant.EMPTY_STRING);
        String base64Str;

        try {
            base64Str = new Base64Utils(authorization).decodeAsString();
        } catch (Exception e) {
            throw new IllegalArgumentException("非法 Token");
        }

        if (!base64Str.contains(":"))
            throw new IllegalArgumentException("非法 Token");

        String[] arr = base64Str.split(":");
        String clientId = arr[0], clientSecret = arr[1];

        return getApp(clientId, clientSecret);
    }

    /**
     * 根据客户端 ID 和密钥获取应用信息。
     *
     * @param clientId     应用的客户端 ID。
     * @param clientSecret 应用的客户端密钥。
     * @return 返回匹配的 App 对象，如果不存在或密钥不合法，则抛出 BusinessException 异常。
     */
    public static App getApp(String clientId, String clientSecret) {
        // 通过 CRUD 操作，查询应用信息，条件是状态为0、客户端 ID 和密钥匹配
        App app = new Action("SELECT * FROM app WHERE stat = 0 AND client_id = ? AND client_secret = ?").query(clientId, clientSecret).one(App.class);

        if (app == null)
            throw new IllegalStateException("App Not found or Illegal clientSecret"); // 如果查询结果为空，表示没有找到对应的应用或密钥不正确，抛出业务异常

        return app;
    }

    public static App getApp(String appId) {
        App app = new Action("SELECT * FROM app WHERE stat = 0 AND client_id = ?").query(appId).one(App.class);

        if (app == null)
            throw new UnsupportedOperationException("App Not found: " + appId);

        return app;
    }

    /**
     * 对客户端ID和密钥进行基础认证编码。
     * <p>
     * 该方法用于将客户端的ID和密钥组合成一个字符串，并对该字符串进行 Base64 编码，以用于 HTTP 请求中的 Authorization 头，实现基础认证。
     * 基础认证是一种简单的认证方式，其中客户端的ID和密钥以一定格式组合，并进行编码，以此向服务器证明客户端的身份。
     *
     * @param clientId     客户端ID，用于标识客户端
     * @param clientSecret 客户端密钥，用于验证客户端的身份
     * @return 返回编码后的字符串，格式为"Basic base64 编码的客户端ID:客户端密钥"
     */
    public static String encodeClient(String clientId, String clientSecret) {
        return OAuthTools.encodeClient(clientId, clientSecret);
    }
}
