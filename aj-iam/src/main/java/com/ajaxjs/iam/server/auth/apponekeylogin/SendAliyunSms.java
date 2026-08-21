package com.ajaxjs.iam.server.auth.apponekeylogin;

import com.ajaxjs.util.JsonUtil;
import com.ajaxjs.util.RandomTools;
import com.ajaxjs.util.date.DateTools;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.TreeMap;

/**
 * 阿里云发送短信
 */
@Slf4j
public class SendAliyunSms extends BaseAliOpenApi {
    public static String send(AliyunSmsEntity entity) {
        // 1. 构造公共请求参数
        Map<String, String> commonParams = new TreeMap<>();
        commonParams.put("Action", "SendSmsVerifyCode");
        commonParams.put("Version", "2017-05-25");
        commonParams.put("Format", "JSON"); // 可选
        commonParams.put("AccessKeyId", entity.getAccessKeyId());
        commonParams.put("Timestamp", DateTools.newISO8601Date());
        commonParams.put("SignatureMethod", SIGNATURE_METHOD);
        commonParams.put("SignatureVersion", SIGNATURE_VERSION);
        commonParams.put("SignatureNonce", RandomTools.generateRandomString(10));

        // 2. 构造业务参数 (如果有的话，请在此处添加)
        // 例如: commonParams.put("Token", "xxx");
        Map<String, String> businessParams = new TreeMap<>(); // 示例：无业务参数
        businessParams.put("PhoneNumber", entity.getPhoneNumbers());
        businessParams.put("SignName", entity.getSignName());
        businessParams.put("TemplateParam", entity.getTemplateParam());
        businessParams.put("TemplateCode", entity.getTemplateCode());
        log.info("entity: {}", entity);

        // 合并公共参数和业务参数
        Map<String, String> allParams = new TreeMap<>(commonParams);
        allParams.putAll(businessParams);

        // 3. 计算签名
        String signature = calculateSignature(allParams, entity.getAccessSecret());
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

        if ("OK".equals(map.get("Code")))
            return "OK";
        else {
            String msg = map.get("Message").toString();

            if ("check frequency failed".equals(msg))
                return "请勿频繁发送验证码";

            return msg;
        }
    }
}