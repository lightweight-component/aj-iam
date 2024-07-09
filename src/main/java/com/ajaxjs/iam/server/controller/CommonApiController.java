package com.ajaxjs.iam.server.controller;

import com.ajaxjs.data.data_service.DataServiceController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/common_api")
public interface CommonApiController extends DataServiceController {
}