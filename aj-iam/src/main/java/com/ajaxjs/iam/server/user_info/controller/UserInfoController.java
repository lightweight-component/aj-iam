package com.ajaxjs.iam.server.user_info.controller;

import com.ajaxjs.iam.annotation.ClientAuthentication;
import com.ajaxjs.iam.server.model.User;
import com.ajaxjs.spring.annotation.BizAction;
import org.springframework.web.bind.annotation.*;

/**
 * 用户信息的维护
 * 必须通过客户端认证调用该接口
 */
@RestController
@RequestMapping("")
public interface UserInfoController {
    /**
     * 获取用户详情
     *
     * @param id 用户 id
     * @return 用户详情，如果找不到则返回 null
     */
    @GetMapping("/{id}")
    @BizAction("获取用户详情")
    @ClientAuthentication
    User info(@PathVariable Long id);

    /**
     * 根据任意字段及值来查询用户
     *
     * @param field 用户名/邮箱/手机
     * @param value 值
     * @return 用户详情，如果找不到则返回 null
     */
    @GetMapping("/any_query")
    @BizAction("根据任意字段及值来查询用户")
    @ClientAuthentication
    User getUserInfoByAnyField(@RequestParam String field, @RequestParam String value);

    /**
     * 修改用户
     *
     * @param user 用户详情
     * @return 是否成功
     */
    @PutMapping
    @ClientAuthentication
    @BizAction("修改用户")
    boolean update(@RequestBody User user);

    /**
     * 注销用户账号
     *
     * @param id 用户 id
     * @return 是否成功
     */
    @DeleteMapping("/{id}")
    @BizAction("注销用户账号")
    @ClientAuthentication
    boolean delete(@PathVariable Long id);

    /**
     * 根据租户获取用户总数
     *
     * @return 用户总数
     */
    @GetMapping("/getTotalUserNumberByTenant")
    @BizAction("获取用户总数")
    @ClientAuthentication
    Long getTotalUserNumberByTenant();
}
