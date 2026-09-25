package com.ajaxjs.iam.server.auth.wechat;

import com.ajaxjs.framework.database.EnableTransaction;
import com.ajaxjs.iam.client.SecurityManager;
import com.ajaxjs.iam.jwt.JwtToken;
import com.ajaxjs.iam.model.App;
import com.ajaxjs.iam.model.SimpleUser;
import com.ajaxjs.iam.server.auth.controller.WechatController;
import com.ajaxjs.iam.server.model.User;
import com.ajaxjs.iam.server.model.UserAccount;
import com.ajaxjs.iam.server.model.UserFunction;
import com.ajaxjs.iam.server.model.wechat.*;
import com.ajaxjs.iam.server.service.ClientCredential;
import com.ajaxjs.iam.server.service.TenantService;
import com.ajaxjs.iam.server.user_info.UserInfoService;
import com.ajaxjs.sqlman.Action;
import com.ajaxjs.util.JsonUtil;
import com.ajaxjs.util.ObjectHelper;
import com.ajaxjs.util.cryptography.aes.AesCbc;
import com.ajaxjs.util.httpremote.Get;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class WechatService extends BaseWechatService implements WechatController {
    @Override
    @EnableTransaction
    public JwtToken miniAppLogin(WechatAuthCode data) {
        Code2SessionResult session = getOpenIdByCode(data);
        App app = ClientCredential.getApp(data.getAppId());

        return createOrUpdateUser(session.getOpenid(), session.getSession_key(), app, app.getTenantId());
    }

    @Override
    @EnableTransaction
    public boolean bindWechat2User(WechatAuthCode data) {
        Code2SessionResult session = getOpenIdByCode(data);

        // check the openId if it's occupied by another user
        if (new Action("SELECT * FROM user_account WHERE identifier = ? AND type = 'WECHAT_MINI'").query(session.getOpenid()).one() != null)
            throw new IllegalStateException("该微信账号已注册过。");

        SimpleUser currentUser = SecurityManager.getUser();

        // check if the account exists
        if (new Action("SELECT * FROM user_account WHERE user_id = ? AND type = 'WECHAT_MINI'").query(currentUser.getId()).one() != null)
            throw new IllegalStateException("该用户 " + currentUser.getName() + " 已绑定微信账号");

        User user = UserInfoService.getUserByIdSimple(currentUser.getId());
        User updateBindState = new User();
        updateBindState.setId(user.getId());
        updateBindState.setBindState(user.getBindState() + UserFunction.BindState.WECHAT);

        new Action(updateBindState).update();

        return createUserAccount(currentUser.getId(), session);
    }

    @Override
    public MiniAppPhoneNumber getMiniAppPhoneNumber(PhoneChangeDTO dto) {
        SimpleUser user = SecurityManager.getUser();
        String sessionKey = new Action("SELECT identifier2 FROM user_account WHERE user_id = ? AND type = 'WECHAT_MINI'").query(user.getId()).oneValue(String.class);

        if (ObjectHelper.isEmptyText(sessionKey))
            throw new IllegalArgumentException("用户" + user.getName() + "没有微信小程序的 session key");

        String cipherText = dto.getCipherText(), iv = dto.getIv();
        String json = new AesCbc(sessionKey).decrypt(cipherText, iv);

        return JsonUtil.fromJson(json, MiniAppPhoneNumber.class);
    }

    static String EXIST_USER_SQL = "SELECT u.*, a.identifier FROM user u LEFT JOIN user_account a ON u.id = a.user_id WHERE u.phone = ? AND a.identifier = ?";

    @Override
    @EnableTransaction
    public JwtToken loginByMiniAppPhoneNumber(PhoneNumberLoginDTO dto) {
        WechatAuthCode wechatAuthCode = new WechatAuthCode();
        wechatAuthCode.setAppId(dto.getAppId());
        wechatAuthCode.setCode(dto.getCode());

        Code2SessionResult openIdByCode = getOpenIdByCode(wechatAuthCode);

        String sessionKey = openIdByCode.getSession_key();
        String iv = dto.getIv();
        String cipherText = dto.getData();
        String json = new AesCbc(sessionKey).decrypt(cipherText, iv);

        MiniAppPhoneNumber phoneNumber = JsonUtil.fromJson(json, MiniAppPhoneNumber.class);
        String phone = phoneNumber.getPhoneNumber();
        log.info("phone:{}", phone);
        String openId = openIdByCode.getOpenid();
        Integer tenantId = TenantService.getTenantId(false);

        User user;
        User existUser = new Action("SELECT * FROM user WHERE stat = 0 AND phone = ? AND tenant_id = ?").query(phone, tenantId).one(User.class);
        boolean isNewlyUser = existUser == null;

        if (isNewlyUser) { // to create a new user
            user = createUser(tenantId.longValue(), phone);
            createUserAccount(user.getId(), openIdByCode);
            phoneNumber.setIsNewlyUser(true);
        } else {
            user = existUser;
            phoneNumber.setIsNewlyUser(false);
            UserAccount existAccount = new Action("SELECT * FROM user_account WHERE identifier = ? AND user_id = ? AND type = 'WECHAT_MINI' AND stat= 0")
                    .query(openId, existUser.getId()).one(UserAccount.class);

            if (existAccount == null)
                createUserAccount(existUser.getId(), openIdByCode);
        }

        return createToken(user, ClientCredential.getApp(dto.getAppId()), isNewlyUser);
    }

    final OpenAccount openAccount;

    @Override
    public void H5OauthLogin(String code, String state) {
        openAccount.login(code, state);
    }

    private final static String LOGIN_API = "https://api.weixin.qq.com/sns/jscode2session?grant_type=authorization_code&appid=%s&secret=%s&js_code=%s";

    /**
     * 通过微信小程序的 code 获取 openId
     *
     * @param data 包含 code 的数据
     * @return 结果
     */
    static Code2SessionResult getOpenIdByCode(WechatAuthCode data) {
        Map<String, Object> query = new Action("SELECT app_id, app_secret FROM app_secret_mgr WHERE owner = ?").query(data.getAppId()).one();

        if (query == null)
            throw new NullPointerException("Please provide the App information.");

        String url = String.format(LOGIN_API, query.get("appId"), query.get("appSecret"), data.getCode());
        Code2SessionResult session = Get.api(url, Code2SessionResult.class);
        log.info("session: {}", session);

        if (session == null || session.getErrcode() != null && session.getErrcode() != 0)
            throw new IllegalStateException(session == null ? "微信登录失败" : "微信登录失败: " + session.getErrmsg());

        return session;
    }
}