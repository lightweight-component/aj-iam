package com.ajaxjs.iam.jwt;


import com.ajaxjs.iam.client.model.TokenValidDetail;
import com.ajaxjs.util.Base64Utils;
import com.ajaxjs.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * JWT 管理器
 * For JWT: 1) Create; 2) Parse(From string to JWebToken object); 3) Validate;
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
    private String issuer = "";

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
        token.setSignature(token.signature(secretKey));

        return token;
    }

    /**
     * 创建 JWT Token with permission
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

    /**
     * To parse the JWT in string format.
     *
     * @param tokenStr JWT Token in string
     * @return JWT object
     */
    public static JWebToken parse(String tokenStr) {
        String[] parts = tokenStr.split("\\.");

        if (parts.length != 3)
            throw new IllegalArgumentException("Invalid Token of: " + tokenStr);

        if (!JWebToken.HEADER.equals(parts[0]))
            throw new IllegalArgumentException("Invalid JWT Header: " + parts[0]);

        String json = new Base64Utils(parts[1]).decodeAsString();
        Payload payload = JsonUtil.fromJson(json, Payload.class);

        if (payload == null)
            throw new IllegalArgumentException("The JWT payload is empty.");

        if (payload.getExp() == null)
            throw new IllegalArgumentException("Payload doesn't contain the field of `exp`. The payload is: " + payload);

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
    public TokenValidDetail validAndDetail(JWebToken token) {
        String _signature = token.signature(secretKey);
        boolean isMatch = token.getSignature().equals(_signature); // signature matched
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
        return validAndDetail(parse(tokenStr)).isValid();
    }
}
