package com.ajaxjs.iam.server.auth.apponekeylogin;

import com.ajaxjs.util.JsonUtil;
import com.ajaxjs.util.ObjectHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

@Slf4j
@Service
public class AliyunOpenApi {
    private static final String ENDPOINT = "https://dypnsapi.aliyuncs.com/";
    private static final String SIGNATURE_METHOD = "HMAC-SHA1";
    private static final String SIGNATURE_VERSION = "1.0";

    @Value("${MobileAppOneKeyLogin.accessKeyId}")
    String accessKeyId;

    @Value("${MobileAppOneKeyLogin.accessKeySecret}")
    String accessKeySecret;

    public String getPhoneByToken(String token) {
        if (ObjectHelper.isEmptyText(accessKeyId) || ObjectHelper.isEmptyText(accessKeySecret))
            throw new UnsupportedOperationException("一键登录配置错误");

        log.info("accessKey: {}", accessKeyId);
        log.info("accessKeySecret: {}", accessKeySecret);

        // 1. 构造公共请求参数
        Map<String, String> commonParams = new TreeMap<>();
        commonParams.put("Action", "GetMobile");
        commonParams.put("Version", "2017-05-25");
        commonParams.put("Format", "JSON"); // 可选
        commonParams.put("AccessKeyId", accessKeyId);
        commonParams.put("SignatureMethod", SIGNATURE_METHOD);
        commonParams.put("Timestamp", generateTimestamp());
        commonParams.put("SignatureVersion", SIGNATURE_VERSION);
        commonParams.put("SignatureNonce", generateNonce());

        // 2. 构造业务参数 (如果有的话，请在此处添加)
        // 例如: commonParams.put("Token", "xxx");
        Map<String, String> businessParams = new TreeMap<>(); // 示例：无业务参数
        businessParams.put("AccessToken", token);

        // 合并公共参数和业务参数
        Map<String, String> allParams = new TreeMap<>(commonParams);
        allParams.putAll(businessParams);

        // 3. 计算签名
        String signature = calculateSignature(allParams, accessKeySecret);
        allParams.put("Signature", signature);

        // 4. 将参数转换为 form-urlencoded 格式
        String requestBody = buildFormData(allParams);

        // 5. 发送 HTTP 请求
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT))
                .header("Content-Type", "application/x-www-form-urlencoded") // 重要：指定内容类型
                .POST(HttpRequest.BodyPublishers.ofString(requestBody)) // POST 请求体
                .build();

        HttpResponse<String> response;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("Response Status Code: {}", response.statusCode());
        String json = response.body();
        log.info("Response Body: {}", json);
        Map<String, Object> map = JsonUtil.json2map(json);
        Map<String, Object> map2 = (Map<String, Object>) map.get("GetMobileResultDTO");

        return map2.get("Mobile").toString();
    }

    /**
     * 生成 ISO8601 格式的 UTC 时间戳
     */
    private static String generateTimestamp() {
        return Instant.now().atOffset(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
    }

    /**
     * 生成唯一的随机数 (SignatureNonce)
     */
    private static String generateNonce() {
        Random random = new Random();
        return String.valueOf(Math.abs(random.nextLong()));
    }

    /**
     * 计算阿里云 OpenAPI 签名
     */
    private static String calculateSignature(Map<String, String> params, String accessKeySecret) {
        StringBuilder canonicalizedQueryString = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            // 注意：签名计算时，Signature 参数本身不参与
            if ("Signature".equals(entry.getKey())) continue;

            canonicalizedQueryString.append("&")
                    .append(percentEncode(entry.getKey())).append("=")
                    .append(percentEncode(entry.getValue()));
        }
        // 移除第一个 &
        if (!canonicalizedQueryString.isEmpty())
            canonicalizedQueryString.deleteCharAt(0);

        String stringToSign = "POST&" + percentEncode("/") + "&" + percentEncode(canonicalizedQueryString.toString());

        String key = accessKeySecret + "&"; // 阿里云要求在Secret后面加 '&'

        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA1"));
            byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(signData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * URL 编码函数，遵循阿里云规范 (使用 %2F 而非 %20)
     */
    private static String percentEncode(String value) {
        if (value == null)
            return "";

        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8)
                    .replace("+", "%20")
                    .replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 构建 application/x-www-form-urlencoded 格式的请求体
     */
    private static String buildFormData(Map<String, String> params) {
        StringBuilder formData = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            formData.append("&")
                    .append(percentEncode(entry.getKey()))
                    .append("=")
                    .append(percentEncode(entry.getValue()));
        }

        if (!formData.isEmpty())
            formData.deleteCharAt(0); // Remove leading &

        return formData.toString();
    }
}