package com.ajaxjs.iam.server.controller;


import com.ajaxjs.dataservice.core.DataServiceController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/common_api")
public interface CommonApiController extends DataServiceController {
}