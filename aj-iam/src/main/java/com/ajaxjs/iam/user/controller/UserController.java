package com.ajaxjs.iam.user.controller;

import com.ajaxjs.iam.user.model.User;
import org.springframework.web.bind.annotation.*;

/**
 * 用户相关控制器
 */
@RestController
@RequestMapping("/user")
public interface UserController {
    /**
     * 获取当前用户详情
     *
     * @return 用户详情
     */
    @CrossOrigin
    @GetMapping("/info")
    User currentUserInfo();

    /**
     * 获取用户详情
     *
     * @param id 用户 id
     * @return 用户详情
     */
    @GetMapping("/{id}")
    User info(@PathVariable Long id);

    /**
     * 通过客户端认证，根据字段查询用户
     *
     * @param authorization 客户端认证
     * @param field         用户名/邮箱/手机
     * @param value         值
     * @return 用户详情
     */
    @GetMapping("/by_client")
    User queryUserByClient(@RequestHeader("authorization") String authorization, @RequestParam String field, @RequestParam String value);

    /**
     * 获取用户详情（根据登录用户 Session）
     *
     * @return 用户详情
     */
    @GetMapping
    User info();

    /**
     * 修改用户（根据登录用户 Session）
     *
     * @param user 用户详情
     * @return 是否成功
     */
    @PostMapping("/update")
    Boolean updateBySession(User user);

    /**
     * 修改用户
     *
     * @param user 用户详情
     * @return 是否成功
     */
    @PutMapping
    Boolean update(User user);

    /**
     * 注销用户账号
     *
     * @param id 用户 id
     * @return 是否成功
     */
    @DeleteMapping("/{id}")
    Boolean delete(Long id);
}
