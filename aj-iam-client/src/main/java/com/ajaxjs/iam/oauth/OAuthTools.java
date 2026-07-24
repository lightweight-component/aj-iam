package com.ajaxjs.iam.oauth;

import com.ajaxjs.util.Base64Utils;
import com.ajaxjs.util.ObjectHelper;

/**
 * OAuth 2.0 中的客户端凭证（Client Credentials）授权模式请求
 */
public class OAuthTools  {
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
        if (ObjectHelper.isEmptyText(clientId) || ObjectHelper.isEmptyText(clientSecret))
            throw new IllegalArgumentException("Missing the arguments: clientId/clientSecret");

        String clientAndSecret = clientId + ":" + clientSecret;

        return "Basic " + new Base64Utils(clientAndSecret).encodeAsString();
    }
}
