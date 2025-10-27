package com.ajaxjs.iam.jwt;


import com.ajaxjs.iam.client.model.TokenValidDetail;
import com.ajaxjs.util.Base64Utils;
import com.ajaxjs.util.HashHelper;
import com.ajaxjs.util.JsonUtil;
import com.ajaxjs.util.ObjectHelper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * JWT 管理器
 */
@Data
@Slf4j
public class JWebTokenMgr {
    /**
     * 密钥
     */
    private String secretKey = "Df87sD#$%#A";

    /**
     * 颁发者
     */
    private String issuer = "foo@bar.net";

    /**
     * 解析 Token
     *
     * @param tokenStr JWT Token
     */
    public JWebToken parse(String tokenStr) {
        String[] parts = tokenStr.split("\\.");

        if (parts.length != 3)
            throw new IllegalArgumentException("无效 Token 格式");

        if (!JWebToken.encodedHeader.equals(parts[0]))
            throw new IllegalArgumentException("非法的 JWT Header: " + parts[0]);

        String json = new Base64Utils(parts[1]).decodeAsString();
        Payload payload = JsonUtil.fromJson(json, Payload.class);

        if (payload == null)
            throw new RuntimeException("Payload is Empty: ");

        if (payload.getExp() == null)
            throw new RuntimeException("Payload 不包含过期字段 exp：" + payload);

        JWebToken token = new JWebToken(payload);
        token.setSignature(parts[2]);
        token.setPayloadJson(json);

        return token;
    }

    /**
     * 校验是否合法的 Token
     *
     * @param token 待检验的 Token
     * @return 是否合法
     */
    public boolean isValid(JWebToken token) {
        String _token = signature(token);
        boolean isMatch = token.getSignature().equals(_token); // signature matched
        Long exp = token.getPayload().getExp();

        if (exp == 0L) /* 0 表示永不过期，不用检查是否超时 */
            return isMatch;
        else {
            boolean isExp = exp < JwtUtils.now(); // true = token expired

            if (isExp) {
                log.debug("超时:" + exp + ", now:" + JwtUtils.now());
                log.debug("超时:" + JwtUtils.toRealTime(exp) + ", now:" + JwtUtils.toRealTime(JwtUtils.now()));
            }

            return !isExp && isMatch;
        }
    }

    public TokenValidDetail validAndDetail(JWebToken token) {
        String _token = signature(token);
        boolean isMatch = token.getSignature().equals(_token); // signature matched
        Long exp = token.getPayload().getExp();

        TokenValidDetail detail = new TokenValidDetail();

        if (exp == 0L) { /* 0 表示永不过期，不用检查是否超时 */
            detail.setValid(true);
            detail.setExpired(false);
            detail.setExpiredTime(0L);
        } else {
            boolean isExp = exp < JwtUtils.now(); // true = token expired

            if (isExp) {
                log.debug("超时:" + exp + ", now:" + JwtUtils.now());
                log.debug("超时:" + JwtUtils.toRealTime(exp) + ", now:" + JwtUtils.toRealTime(JwtUtils.now()));
            }

            boolean isValid = !isExp && isMatch;

            detail.setValid(isValid);
            detail.setExpired(isExp);
            detail.setExpiredTime(exp);
        }

        return detail;
    }

    /**
     * 校验是否合法的 Token
     *
     * @param tokenStr 待检验的 Token
     * @return 是否合法
     */
    public boolean isValid(String tokenStr) {
        return isValid(parse(tokenStr));
    }

    /**
     * 生成签名
     *
     * @param token Token
     * @return 签名
     */
    public String signature(JWebToken token) {
        String headerPayload = token.headerPayload();

        if (ObjectHelper.isEmptyText(headerPayload))
            throw new IllegalArgumentException("头 Payload 参数有问题");

//        return new MessageDigestHelper().setAlgorithmName("HmacSHA256").setKey(secretKey).
//                setValue(headerPayload).setBase64withoutPadding(true).setHexStr(false).getResult();
        return new HashHelper("HmacSHA256", headerPayload).setKey(secretKey).hashAsBase64(true);
    }

    /**
     * 创建 JWT Token
     *
     * @param payload Payload 实例或其子类
     * @return JWT Token
     */
    public JWebToken tokenFactory(Payload payload) {
        payload.setIat(JwtUtils.now());
        payload.setIss(issuer);

        JWebToken token = new JWebToken(payload);
        token.setPayloadJson(JsonUtil.toJson(payload));
        token.setSignature(signature(token));

        return token;
    }

    /**
     * 创建 JWT Token
     *
     * @param sub                    用户 ID
     * @param name                   用户名称
     * @param aud                    角色的意思，可为多个
     * @param expires                过期时间
     * @param tenantId               租户 id
     * @param permissionValues       权限值列表
     * @param modulePermissionValues 模块权限值列表
     * @return JWT Token
     */
    public JWebToken tokenFactory(String sub, String name, String aud, long expires, Integer tenantId, Long[] permissionValues, Long[] modulePermissionValues) {
        Payload payload = new Payload();
        payload.setSub(sub);
        payload.setName(name);
        payload.setAud(aud);
        payload.setExp(expires);
        payload.setT(tenantId);
        payload.setP(permissionValues);
        payload.setMP(modulePermissionValues);

        return tokenFactory(payload);
    }
}
