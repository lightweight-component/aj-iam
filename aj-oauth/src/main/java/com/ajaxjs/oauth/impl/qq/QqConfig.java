package com.ajaxjs.oauth.impl.qq;

import com.ajaxjs.oauth.model.Config;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class QqConfig extends Config {
    /**
     * 是否需要申请 unionId，目前只针对 QQ 登录
     * 注：QQ 授权登录时，获取 unionId 需要单独发送邮件申请权限。如果个人开发者账号中申请了该权限，可以将该值置为 true，在获取 openId 时就会同步获取 unionId
     * 参考链接：<a href="http://wiki.connect.qq.com/unionid%E4%BB%8B%E7%BB%8D">...</a>
     */
    private boolean unionId;
}
