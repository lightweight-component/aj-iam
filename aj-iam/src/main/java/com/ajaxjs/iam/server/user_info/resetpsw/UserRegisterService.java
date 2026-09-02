package com.ajaxjs.iam.server.user_info.resetpsw;

import com.ajaxjs.framework.database.EnableTransaction;
import com.ajaxjs.iam.UserConstants;
import com.ajaxjs.iam.model.App;
import com.ajaxjs.iam.server.model.Tenant;
import com.ajaxjs.iam.server.model.UserAccount;
import com.ajaxjs.iam.server.model.UserAccountType;
import com.ajaxjs.iam.server.model.UserFunction;
import com.ajaxjs.iam.server.service.ClientCredential;
import com.ajaxjs.iam.server.service.TenantService;
import com.ajaxjs.iam.server.service.password.CheckStrength;
import com.ajaxjs.iam.server.user_info.controller.UserRegisterController;
import com.ajaxjs.security.iplist.IpList;
import com.ajaxjs.spring.DiContextUtil;
import com.ajaxjs.sqlman.Action;
import com.ajaxjs.sqlman.model.CreateResult;
import com.ajaxjs.sqlman.util.SnowflakeId;
import com.ajaxjs.sqlman.util.Utils;
import com.ajaxjs.util.ObjectHelper;
import com.ajaxjs.util.RandomTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;

@Service
@Slf4j
public class UserRegisterService implements UserRegisterController {
    @Override
    @EnableTransaction
    public boolean registerWeb(Map<String, Object> params) {
        return _register(params);
    }

    @Override
    @EnableTransaction
    public boolean register(Map<String, Object> params) {
        return _register(params);
    }


    @Override
    public boolean checkRepeat(String field, String value) {
        return isRepeat(field, value, TenantService.getTenantId());
    }

    @Autowired
    @Qualifier("passwordEncode")
    Function<String, String> passwordEncode;

    @Value("${auth.user.CheckStrength:true}")
    boolean isCheckPasswordStrength;

    public boolean _register(Map<String, Object> params) {
        // 所有字符串 trim 一下
        for (String key : params.keySet()) {
            Object obj = params.get(key);

            if (obj instanceof String)
                params.put(key, obj.toString().trim());
        }

        // 校验
        if (isNull(params, "password"))
            throw new IllegalArgumentException("注册密码不能为空");

        boolean hasNoUsername = isNull(params, "loginId"), hasNoEmail = isNull(params, "email"), hasNoPhone = isNull(params, "phone");
        if (hasNoUsername && hasNoEmail && hasNoPhone)
            throw new IllegalArgumentException("没有用户标识， loginId/email/phone 至少填一种");

        int tenantId = getTenantId(params);

        // 是否重复
        if (!hasNoUsername && isRepeat("login_id", params.get("loginId").toString(), tenantId))
            throw new IllegalArgumentException("用户名 loginId: " + params.get("loginId").toString() + " 重复");

        if (!hasNoEmail && isRepeat("email", params.get("email").toString(), tenantId))
            throw new IllegalArgumentException("邮箱: " + params.get("email").toString() + " 重复");

        if (!hasNoPhone && isRepeat("phone", params.get("phone").toString(), tenantId))
            throw new IllegalArgumentException("手机: " + params.get("phone").toString() + " 重复");

        // 获取业务自定义的字段，存在 extract json 中
        Map<String, Object> extract = new HashMap<>();
        List<String> basicFields = Arrays.asList("loginId", "email", "phone", "password", "tenantId");
//        final Map<String, Object> _params = new HashMap<>(params);
//
//        _params.forEach((key, value) -> {
//            if (!basicFields.contains(key)) {
//                _params.remove(key);
//                extract.put(key, value);
//            }
//        });

        params.entrySet().removeIf(entry -> {
            String key = entry.getKey();
            Object value = entry.getValue();
            boolean isRemove = !basicFields.contains(key);

            if (isRemove && value != null && (value instanceof String && ObjectHelper.hasText(value.toString())))
                extract.put(key, value);

            return isRemove;
        });

        if (!ObjectUtils.isEmpty(extract))
            params.put("extend", extract);

        // 有些字段不要
        String psw = params.get("password").toString();
        params.remove("password");

        // 检测密码强度
        if (isCheckPasswordStrength) {
            CheckStrength.LEVEL passwordLevel = CheckStrength.getPasswordLevel(psw);

            if (passwordLevel == CheckStrength.LEVEL.EASY)
                throw new UnsupportedOperationException("密码强度太低");
        }

        if (!params.containsKey("loginId"))
            params.put("loginId", createDefaultUserLoginId());

        params.put("uid", SnowflakeId.get());
        params.put("bindState", UserFunction.BindState.IAM);
        params = Utils.changeFieldToColumnName(params);

        long userId = new Action(params, "user").create().execute(true, Long.class).getNewlyId(); // 写入数据库

        saveUserRole(userId, tenantId);

        UserAccount auth = new UserAccount();
        auth.setUserId(userId);
        auth.setPassword(passwordEncode.apply(psw));
        auth.setRegisterType(UserConstants.LoginType.PASSWORD);
        auth.setType(UserAccountType.PASSWORD);
        auth.setRegisterIp(IpList.getClientIp(Objects.requireNonNull(DiContextUtil.getRequest())));

        return new Action(auth, "user_account").create().execute(true, Long.class).isOk();
    }

    private static final String TENANT_NAME = "tenantName";

    private static final String TENANT_CODE = "tenantCode";

    /**
     * Get the id of tenant for any cases.
     *
     * @param params The request data
     * @return The tenant id
     */
    int getTenantId(Map<String, Object> params) {
        int tenantId;

        if (params.containsKey(TENANT_NAME) && params.containsKey(TENANT_CODE)) {
            String tenantCode = params.get(TENANT_CODE).toString();
            Tenant tenant = new Action("SELECT * FROM tenant WHERE code = ? AND stat = 0").query(tenantCode).one(Tenant.class);

            if (tenant == null) {
                // create if not exists
                tenant = new Tenant();
                tenant.setName(params.get(TENANT_NAME).toString());
                tenant.setCode(tenantCode);
                tenant.setDefaultRoleId(45);

                CreateResult<Integer> execute = new Action(tenant).create().execute(true, Integer.class);

                if (!execute.isOk())
                    throw new NullPointerException("创建租户失败");

                tenantId = execute.getNewlyId();
            } else
                tenantId = tenant.getId().intValue();

            params.remove(TENANT_NAME);
            params.remove(TENANT_CODE);
        } else {
            String clientId;

            try {
                clientId = ClientCredential.getAppId();
            } catch (NullPointerException ignored) {
                clientId = null;
            }

            if (clientId != null) {
                App app = ClientCredential.getApp(clientId);
                tenantId = app.getTenantId();
            } else {
                Integer _c = TenantService.getTenantId(false);
                tenantId = _c == null ? 0 : _c;
            }
        }

        if (tenantId == 0)
            throw new IllegalArgumentException("租户 id 不能为空");
        else {
            params.put("tenantId", tenantId);

            return tenantId;
        }
    }

    private static boolean isNull(Map<String, Object> params, String key) {
        return !params.containsKey(key) || !StringUtils.hasText(params.get(key).toString());
    }

    /**
     * 保存用户角色
     */
    private static void saveUserRole(long userId, int tenantId) {
        String sql = "INSERT INTO per_user_role (user_id, role_id)\n" +
                "(SELECT ?, default_role_id FROM tenant WHERE id = ? AND default_role_id IS NOT NULL)";
        // default_role_id 如果为空则不插入新数据（用户没角色）
        boolean isOk = new Action(sql).create(userId, tenantId).execute(true, Long.class).isOk();

        if (isOk)
            log.info("保存用户角色成功！");
        else
            log.warn("保存用户角色失败！");
    }

    /**
     * 检查某个值是否已经存在一样的值
     *
     * @param field 数据库里面的字段名称
     * @param value 欲检查的值
     * @return true=值重复
     */
    public static boolean isRepeat(String field, String value, int tenantId) {
        String sql = "SELECT id FROM user WHERE stat != 1 AND %s = ? AND tenant_id = ? LIMIT 1";
        sql = String.format(sql, field.trim());

        return new Action(sql).query(value.trim(), tenantId).oneValue(Long.class) != null; // 有这个数据表示重复
    }

    /**
     * Give a user loginId if not provided
     *
     * @return Random loginId
     */
    public static String createDefaultUserLoginId() {
        return "User_" + RandomTools.generateRandomString(5);
    }
}
