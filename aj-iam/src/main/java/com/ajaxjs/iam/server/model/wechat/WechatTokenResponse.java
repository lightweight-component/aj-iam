package com.ajaxjs.iam.server.model.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 微信开放平台 - 网页授权 Access Token 响应实体
 * 对应接口：通过 code 换取网页授权 access_token
 * <a href="https://developers.weixin.qq.com/doc/oplatform/Website_App/WeChat_Login/Authorized_Interface_Calling_UnionID.html">...</a>
 */
@Data
public class WechatTokenResponse {
    /**
     * 接口调用凭证
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * access_token 接口调用凭证超时时间，单位（秒）
     * 通常为 7200 秒
     */
    @JsonProperty("expires_in")
    private Long expiresIn;

    /**
     * 用户刷新 access_token 的凭证
     * 有效期通常为 30 天，用于在 access_token 过期后获取新的 token，而无需用户重新授权
     */
    @JsonProperty("refresh_token")
    private String refreshToken;

    /**
     * 授权用户唯一标识
     * 针对当前公众号/小程序 AppID 的唯一用户 ID
     */
    @JsonProperty("openid")
    private String OpenId;

    /**
     * 用户授权的作用域
     * 使用逗号(,)分隔，例如：snsapi_userinfo,snsapi_base
     */
    private String scope;

    /**
     * 用户统一标识
     * 只有当账号绑定了微信开放平台后才会返回。
     * 针对同一个微信开放平台账号下的不同应用（公众号、小程序、App），同一用户的 unionid 是唯一的
     */
    @JsonProperty("unionid")
    private String unionId;

    /**
     * 错误码（正常成功时不返回，出错时返回）
     */
    @JsonProperty("errcode")
    private Integer errCode;

    /**
     * 错误信息（正常成功时不返回，出错时返回）
     */
    @JsonProperty("errmsg")
    private String errMsg;
}