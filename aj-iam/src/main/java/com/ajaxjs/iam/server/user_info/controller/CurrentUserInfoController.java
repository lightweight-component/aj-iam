package com.ajaxjs.iam.server.user_info.controller;

import com.ajaxjs.fileupload.UploadedResult;
import com.ajaxjs.framework.mvc.unifiedreturn.PureOutput;
import com.ajaxjs.iam.server.model.User;
import com.ajaxjs.iam.server.model.UserAccount;
import com.ajaxjs.spring.annotation.BizAction;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 用户信息的维护，仅限当前用户使用
 */
@RestController
@RequestMapping("/curr_user")
public interface CurrentUserInfoController {
    /**
     * 获取当前用户详情
     *
     * @return 用户详情
     */
    @GetMapping
    @BizAction("获取当前用户详情")
    User currentUserInfo();

    /**
     * 获取当前用户详情（JSONP）
     *
     * @return 用户详情
     */
    @BizAction("获取当前用户详情（JSONP）")
    @GetMapping(value = "/jsonp", produces = "application/javascript;charset=UTF-8")
    @PureOutput
    String currentUserInfoJSONP(@RequestParam String callback);

    /**
     * 修改用户
     *
     * @param user 用户详情
     * @return 是否成功
     */
    @PutMapping
    boolean update(@RequestBody User user);

    /**
     * 注销用户账号
     *
     * @return 是否成功
     */
    @DeleteMapping
    boolean delete();

    /**
     * 修改用户头像
     *
     * @return 是否成功
     */
    @BizAction("修改用户头像")
    @PostMapping(value = "/avatar", consumes = "multipart/form-data")
    UploadedResult avatar(@RequestParam("file") MultipartFile file);

    /**
     * 获取用户多个账号信息（微信、支付宝……）
     *
     * @return 用户多个账号信息
     */
    @GetMapping("/accounts")
    @BizAction("用户多个账号信息")
    List<UserAccount> getUserAccountInfo();
}
