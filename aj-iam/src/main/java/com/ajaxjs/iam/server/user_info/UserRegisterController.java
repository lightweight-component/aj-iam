package com.ajaxjs.iam.server.user_info;

import com.ajaxjs.iam.annotation.AllowOpenAccess;
import com.ajaxjs.spring.annotation.BizAction;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 用户注册
 */
@RestController
@RequestMapping("/user_register")
public interface UserRegisterController {
    /**
     * 用户注册
     * <p>
     *F
     * @param params 用户参数
     * @return 是否成功
     */
    @PostMapping("/register")
    @AllowOpenAccess
//    @ImageCaptchaCheck
    @BizAction("用户注册")
    boolean register(@RequestBody Map<String, Object> params);
}
