package com.ajaxjs.iam.server.service;

import com.ajaxjs.framework.database.EnableTransaction;
import com.ajaxjs.framework.wechat.applet.AppletService;
import com.ajaxjs.framework.wechat.applet.model.Code2SessionResult;
import com.ajaxjs.iam.client.SecurityManager;
import com.ajaxjs.iam.jwt.JWebTokenMgr;
import com.ajaxjs.iam.jwt.JwtUtils;
import com.ajaxjs.iam.model.SimpleUser;
import com.ajaxjs.iam.server.common.IamConstants;
import com.ajaxjs.iam.server.controller.WechatController;
import com.ajaxjs.iam.server.model.JwtAccessToken;
import com.ajaxjs.iam.server.model.WechatAuthCode;
import com.ajaxjs.iam.server.model.po.App;
import com.ajaxjs.iam.server.model.wechat.MiniAppPhoneNumber;
import com.ajaxjs.iam.user.model.User;
import com.ajaxjs.iam.user.model.UserAccount;
import com.ajaxjs.iam.user.model.UserAccountType;
import com.ajaxjs.iam.user.service.UserFunction;
import com.ajaxjs.iam.user.service.UserService;
import com.ajaxjs.sqlman.Sql;
import com.ajaxjs.sqlman.crud.Entity;
import com.ajaxjs.util.EncodeTools;
import com.ajaxjs.util.JsonUtil;
import com.ajaxjs.util.RandomTools;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.cryptography.Constant;
import com.ajaxjs.util.cryptography.Cryptography;
import com.ajaxjs.util.http_request.Get;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Map;

@Service
@Slf4j
public class WechatService extends OAuthCommon implements WechatController {
    @Autowired
    JWebTokenMgr jWebTokenMgr;

    @Value("${User.oidc.jwtExpireHours:74}")
    int jwtExpireHours;

    @Override
    @EnableTransaction
    public JwtAccessToken miniAppLogin(WechatAuthCode data) {
        Code2SessionResult session = getOpenIdByCode(data);
        UserAccount account = Sql.instance().input("SELECT * FROM user_account WHERE identifier = ? AND type = 'WECHAT_MINI'", session.getOpenid()).query(UserAccount.class);
        User user;

        App app = Sql.instance().input("SELECT * FROM app WHERE stat != 1 AND client_id = ?", data.getAppId()).query(App.class);

        if (app == null)
            throw new UnsupportedOperationException("App Not found: " + data.getAppId());

        if (account != null) { // exists account
            Long userId = account.getUserId();
            user = UserService.getUserById(userId);

            // saves session key
            UserAccount saveSessionKey = new UserAccount();
            saveSessionKey.setId(account.getId());
            saveSessionKey.setIdentifier2(session.getSession_key());

            Entity.instance().input(saveSessionKey).update();

            User updateBindState = new User();
            updateBindState.setId(user.getId());
            updateBindState.setBindState(user.getBindState() + UserFunction.BindState.WECHAT);

            Entity.instance().input(updateBindState).update();
        } else { // to create a new account
            user = new User();
            user.setLoginId("WxMiniUser_" + RandomTools.generateRandomString(5));
            user.setTenantId(app.getTenantId().longValue());
            user.setBindState(UserFunction.BindState.WECHAT);

            Long userId = Entity.instance().input(user).create(true, Long.class).getNewlyId();
            user.setId(userId);

            account = new UserAccount();
            account.setUserId(userId);
            account.setIdentifier(session.getOpenid());
            account.setIdentifier2(session.getSession_key());
            account.setType(UserAccountType.WECHAT_MINI);

            Entity.instance().input(account).create();
        }

        // 生成 JWT Token
        JwtAccessToken accessToken = new JwtAccessToken();

        // TODO user.getName() 中文名会乱码
        Long[][] userPermissions = OidcService.getUserPermissions(user.getId());
        String jWebToken = jWebTokenMgr.tokenFactory(
                String.valueOf(user.getId()), user.getLoginId(), "", JwtUtils.setExpire(jwtExpireHours),
                user.getTenantId().intValue(), userPermissions[0], userPermissions[1]
        ).toString();
        accessToken.setId_token(jWebToken);
        createToken(accessToken, app, IamConstants.GrantType.OIDC, user);

        return accessToken;
    }

    @Override
    @EnableTransaction
    public boolean bindWechat2User(WechatAuthCode data) {
        Code2SessionResult session = getOpenIdByCode(data);

        // check the openId if it's occupied by other user
        if (Sql.instance().input("SELECT * FROM user_account WHERE identifier = ? AND type = 'WECHAT_MINI'", session.getOpenid()).query() != null)
            throw new IllegalStateException("该微信账号已注册过。");

        SimpleUser currentUser = SecurityManager.getUser();

        // check if the account exists
        if (Sql.instance().input("SELECT * FROM user_account WHERE user_id = ? AND type = 'WECHAT_MINI'", currentUser.getId()).query() != null)
            throw new IllegalStateException("该用户 " + currentUser.getName() + " 已绑定微信账号");

        User user = UserService.getUserById(currentUser.getId());
        User updateBindState = new User();
        updateBindState.setId(user.getId());
        updateBindState.setBindState(user.getBindState() + UserFunction.BindState.WECHAT);

        Entity.instance().input(updateBindState).update();

        UserAccount account = new UserAccount();
        account.setUserId(currentUser.getId());
        account.setIdentifier(session.getOpenid());
        account.setType(UserAccountType.WECHAT_MINI);

        return Entity.instance().input(account).create().isOk();
    }

    @Override
    public MiniAppPhoneNumber getMiniAppPhoneNumber(Map<String, String> _data) {
        String data = _data.get("data"), iv = _data.get("iv");
        SimpleUser user = SecurityManager.getUser();

        String sessionKey = Sql.instance().input("SELECT identifier2 FROM user_account WHERE user_id = ? AND type = 'WECHAT_MINI'",
                user.getId()).queryOne(String.class);

        if (StrUtil.isEmptyText(sessionKey))
            throw new IllegalArgumentException("用户" + user.getName() + "没有微信小程序的 session key");

        String json = aesDecryptPhone(iv, data, sessionKey);

        return JsonUtil.fromJson(json, MiniAppPhoneNumber.class);
    }

    /**
     * 解密小程序提供的加密数据，返回包含手机号码等信息的 JSON 对象
     *
     * @param iv         前端给的
     * @param cipherText 前端给的，密文
     * @param sessionKey 后端申请返回
     * @return 解密后的文本
     */
    public static String aesDecryptPhone(String iv, String cipherText, String sessionKey) {
        byte[] keyData = EncodeTools.base64Decode(sessionKey);

        Cryptography cryptography = new Cryptography(Constant.AES_WX_MINI_APP, Cipher.DECRYPT_MODE);
        cryptography.setKey(new SecretKeySpec(keyData, Constant.AES)); // little odd, it's AES, differs with AES_WX_MINI_APP.
        cryptography.setSpec(new IvParameterSpec(EncodeTools.base64Decode(iv)));
        cryptography.setDataStrBase64(cipherText);

        return cryptography.doCipherAsStr();
    }

    /**
     * 通过微信小程序的 code 获取 openId
     *
     * @param data 包含 code 的数据
     * @return 结果
     */
    static Code2SessionResult getOpenIdByCode(WechatAuthCode data) {
        Map<String, Object> query = Sql.instance().input("SELECT app_id, app_secret FROM app_secret_mgr WHERE owner = ?", data.getAppId()).query();
        log.info(":::" + query);
        String url = String.format(AppletService.LOGIN_API, query.get("appId"), query.get("appSecret"), data.getCode());
        Code2SessionResult session = Get.api2bean(url, Code2SessionResult.class);
        log.info("session: " + session);

        if (session == null || session.getErrcode() != null && session.getErrcode() != 0)
            throw new IllegalStateException(session == null ? "微信登录失败" : "微信登录失败: " + session.getErrmsg());

        return session;
    }
}
