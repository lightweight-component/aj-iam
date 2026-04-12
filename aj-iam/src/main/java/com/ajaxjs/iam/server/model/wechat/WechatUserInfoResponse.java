package com.ajaxjs.iam.server.model.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 微信开放平台 - 获取用户信息响应实体
 * 对应接口：获取用户信息 (sns/userinfo)
 */
@Data
public class WechatUserInfoResponse {
    /**
     * 用户的唯一标识
     * 针对当前公众号/小程序 AppID 的唯一用户 ID
     */
    @JsonProperty("openid")
    private String openId;

    /**
     * 用户昵称
     */
    @JsonProperty("nickname")
    private String nickname;

    /**
     * 用户的性别
     * 值为 1 时是男性，值为 2 时是女性，值为 0 时是未知
     */
    @JsonProperty("sex")
    private Integer sex;

    /**
     * 用户头像
     * 最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像）
     * 用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
     */
    @JsonProperty("headimgurl")
    private String headImgUrl;

    /**
     * 用户统一标识
     * 只有在管理员将公众号绑定到微信开放平台账号后，才会出现该字段。
     * 针对同一个微信开放平台账号下的不同应用，同一用户的 unionid 是唯一的
     */
    @JsonProperty("unionid")
    private String unionId;

    /**
     * 用户个人资料填写的省份
     */
    @JsonProperty("province")
    private String province;

    /**
     * 普通用户个人资料填写的城市
     */
    @JsonProperty("city")
    private String city;

    /**
     * 国家，如中国为CN
     */
    @JsonProperty("country")
    private String country;

    /**
     * 用户特权信息
     * json 数组，如微信沃卡用户为（chinaunicom）
     */
    @JsonProperty("privilege")
    private List<String> privilege;

    // --- 以下字段用于处理接口报错情况 ---

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