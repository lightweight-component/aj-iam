package com.ajaxjs.iam.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 管理端接口
 */
@RestController
@RequestMapping("/admin")
public interface AdminController {
    @GetMapping("/welcome")
    Map<String, Object> welcome();
}
