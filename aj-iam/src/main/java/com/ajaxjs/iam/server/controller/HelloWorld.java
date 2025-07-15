package com.ajaxjs.iam.server.controller;

import com.ajaxjs.framework.database.IgnoreDataBaseConnect;
import com.ajaxjs.iam.annotation.AllowAccess;
import com.ajaxjs.spring.mvc.unifiedreturn.ResponseResultWrapper;
import org.springframework.http.ResponseEntity;
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
        result.setData("Hello World");

        ResponseEntity l;
        return result;
    }
}
