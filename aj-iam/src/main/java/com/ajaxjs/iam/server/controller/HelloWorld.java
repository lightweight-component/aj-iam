package com.ajaxjs.iam.server.controller;

import com.ajaxjs.framework.database.IgnoreDataBaseConnect;
import com.ajaxjs.iam.annotation.AllowAccess;
import com.ajaxjs.framework.mvc.unifiedreturn.ResponseResultWrapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HelloWorld {
    @GetMapping
    @IgnoreDataBaseConnect
    @AllowAccess
    ResponseResultWrapper sayHi() {
        ResponseResultWrapper result = new ResponseResultWrapper();
        result.setStatus(1);
        result.setData("Hello World");

        return result;
    }
}
