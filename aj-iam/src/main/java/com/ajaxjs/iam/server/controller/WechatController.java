package com.ajaxjs.iam.server.controller;

import com.ajaxjs.framework.mvc.unifiedreturn.BizAction;
import com.ajaxjs.iam.annotation.AllowOpenAccess;
import com.ajaxjs.iam.jwt.JwtAccessToken;
import com.ajaxjs.iam.server.model.WechatAuthCode;
import com.ajaxjs.iam.server.model.wechat.MiniAppPhoneNumber;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 微信相关业务的控制器
 */
@RestController
@RequestMapping("/wechat")
public interface WechatController {
    /**
     * 微信小程序登录
     *
     * @param data 包含 code 的数据
     * @return Token
     */
    @PostMapping("/mini_app_login")
    @BizAction("微信小程序登录")
    @AllowOpenAccess
    JwtAccessToken miniAppLogin(@RequestBody WechatAuthCode data);

    /**
     * 绑定微信账号到现有的账号
     * 需要获取用户的 openId，可以通过调用微信提供的 wx.login API 来获取临时登录凭证 code，并用此 code 向微信服务器换取 openid 和 session_key
     *
     * @param data 包含 code 的数据
     * @return 是否成功
     */
    @PostMapping("/mini_app_bind_account")
    @BizAction("绑定微信账号到现有的账号")
    boolean bindWechat2User(@RequestBody WechatAuthCode data);

    /**
     * 解密小程序提供的加密数据，返回包含手机号码等信息的 JSON 对象
     *
     * @param data 加密数据
     * @return 手机号码等信息的 JSON 对象
     */
    @PostMapping("/phone_number")
    @BizAction("解密小程序提供的加密数据，返回包含手机号码等信息的 JSON 对象")
    MiniAppPhoneNumber getMiniAppPhoneNumber(@RequestBody Map<String, String> data);
}
