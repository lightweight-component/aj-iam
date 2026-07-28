package com.ajaxjs.iam.server.auth.wechat;

import com.ajaxjs.framework.model.BusinessException;
import com.ajaxjs.iam.UserConstants;
import com.ajaxjs.iam.client.BaseOidcClientUserController;
import com.ajaxjs.iam.model.App;
import com.ajaxjs.iam.server.model.wechat.WechatTokenResponse;
import com.ajaxjs.iam.server.model.wechat.WechatUserInfoResponse;
import com.ajaxjs.iam.server.service.ClientCredential;
import com.ajaxjs.iam.server.service.TenantService;
import com.ajaxjs.iam.jwt.JwtToken;
import com.ajaxjs.spring.DiContextUtil;
import com.ajaxjs.sqlman.Action;
import com.ajaxjs.util.ObjectHelper;
import com.ajaxjs.util.httpremote.Get;
import com.ajaxjs.util.httpremote.HttpConstant;
import com.ajaxjs.util.httpremote.Request;
import com.ajaxjs.util.io.DataReader;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 公众号
 */
@Service
@Slf4j
public class OpenAccount extends BaseWechatService {
    private final static String H5_LOGIN =
            "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    private final static String H5_USER_INFO =
            "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";

    public void login(String code, String state) {
        String appId = TenantService.getAppIdd();

        if (ObjectHelper.isEmptyText(appId))
            throw new IllegalArgumentException("请提供 AppId");

        Integer tenantId = TenantService.getTenantId(false);

        if (tenantId == null)
            throw new IllegalArgumentException("请提供 tenantId");

        Map<String, Object> query = new Action("SELECT app_id, app_secret FROM app_secret_mgr WHERE owner = ?").query(tenantId).one();

        if (query == null)
            throw new NullPointerException("Please provide the App Secret information.");

        String url = String.format(H5_LOGIN, query.get("appId"), query.get("appSecret"), code);

        WechatTokenResponse result = Get.api(url, WechatTokenResponse.class);
        log.info("m: {}", result);

        if (result.getErrCode() != null)
            throw new UnsupportedOperationException("获取 JWT Token 失败，原因: " + result.getErrMsg());

        // 获取用户信息
        String openId = result.getOpenId();
        String userInfoUrl = String.format(H5_USER_INFO, result.getAccessToken(), openId);
        WechatUserInfoResponse userInfo = Get.api(userInfoUrl, WechatUserInfoResponse.class);
        log.info("userInfo: {}", userInfo);

        if (userInfo.getErrCode() != null)
            throw new UnsupportedOperationException("获取用户信息失败，原因: " + userInfo.getErrMsg());

        App app = ClientCredential.getApp(appId);
        HttpServletResponse response = DiContextUtil.getResponse();
        JwtToken accessToken = createOrUpdateUser(openId, null, app, tenantId.longValue());
        BaseOidcClientUserController.setTokenToCookie(accessToken, response);

        if (accessToken.getIsNewlyUser())
            updateUserInfo(userInfo, openId);

        String redirectUri = app.getRedirectUri();

        if (ObjectHelper.isEmptyText(redirectUri))
            throw new IllegalArgumentException("未准备好跳转地址");

        log.info("用户登录成功");
        response.encodeRedirectURL(redirectUri);
    }

//    private final static String UPDATE_USER_SQL = "UPDATE user SET username = ?, gender = ?, avatar_blob = ? WHERE id = " +
//            "(SELECT user_id FROM user_account WHERE stat = 0 AND identifier = ?)";

    private final static String UPDATE_USER_SQL = "id = (SELECT user_id FROM user_account WHERE stat = 0 AND identifier = '%s')";

    /**
     * 更新用户相关资料
     */
    private void updateUserInfo(WechatUserInfoResponse userInfo, String openId) {
        Map<String, Object> user = new HashMap<>();

        if (ObjectHelper.hasText(userInfo.getNickname()))
            user.put("username", userInfo.getNickname());

        if (userInfo.getSex() != null)
            user.put("gender", UserConstants.Gender.values()[userInfo.getSex()]);

        if (ObjectHelper.hasText(userInfo.getHeadImgUrl())) {
            log.info("头像: {}", userInfo.getHeadImgUrl());
            user.put("avatar_blob", getUrlImgAsBytes(userInfo.getHeadImgUrl()));
        }

        log.info("user: {}", user);

        if (!new Action(user, "user").update().execute(String.format(UPDATE_USER_SQL, openId)).isOk())
            throw new BusinessException("更新用户相关资料失败");
    }

    static byte[] getUrlImgAsBytes(String url) {
        Request get = new Request(HttpConstant.HttpMethod.GET, url);

        get.init(conn -> {// Set up connection properties
            conn.setDoInput(true);
            conn.setDoOutput(true);
        });

        AtomicReference<byte[]> resultRef = new AtomicReference<>();
        get.setInputStreamConsumer(in -> {
            byte[] bytes = new DataReader(in).readAsBytes();
            resultRef.set(bytes);
        });
        get.connect();

        return resultRef.get();
    }
}