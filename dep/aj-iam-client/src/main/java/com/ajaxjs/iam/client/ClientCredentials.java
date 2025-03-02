package com.ajaxjs.iam.client;


import lombok.Data;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.ajaxjs.iam.jwt.JwtUtils;

/**
 * OAuth 2.0 中的客户端凭证（Client Credentials）授权模式请求
 */
@Data
public class ClientCredentials {
    /**
     * 获取 Token URL
     */
    private String tokenEndPoint;

    /**
     * 客户端 id
     */
    private String clientId;

    /**
     * 客户端密钥
     */
    private String clientSecret;

    private RestTemplate restTemplate;

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
        String clientAndSecret = clientId + ":" + clientSecret;

        return "Basic " + JwtUtils.encodeBase64(clientAndSecret);
    }

    /**
     * 使用基础认证方式请求 Token
     *
     * @param clientId
     * @param clientSecret
     */
    public void requestWithBasic(String clientId, String clientSecret) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", encodeClient(clientId, clientSecret)); // 请求头

        MultiValueMap<String, Object> bodyParams = new LinkedMultiValueMap<>();
        bodyParams.add("grant_type", "client_credentials");

        ResponseEntity<String> responseEntity = restTemplate.exchange(tokenEndPoint, HttpMethod.POST, new HttpEntity<>(bodyParams, headers), String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {

        }
    }
}
