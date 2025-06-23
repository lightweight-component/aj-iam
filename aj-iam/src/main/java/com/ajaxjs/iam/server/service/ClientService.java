package com.ajaxjs.iam.server.service;


import com.ajaxjs.iam.server.model.po.App;
import com.ajaxjs.util.RandomTools;

import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;
import org.springframework.util.ConcurrentLruCache;
import org.springframework.util.StringUtils;

@Service
public class ClientService {
    public Boolean clientRegister(App app) {
        if (!StringUtils.hasText(app.getName()))
            throw new IllegalArgumentException("客户端的名称和回调地址不能为空");

        String clientId = RandomTools.generateRandomString(24);// 生成24位随机的 clientId
        App savedClientDetails = findClientDetailsByClientId(clientId);

        // 生成的 clientId 必须是唯一的，尝试十次避免有重复的 clientId
        for (int i = 0; i < 10; i++) {
            if (savedClientDetails == null)
                break;
            else {
                clientId = RandomTools.generateRandomString(24);
                savedClientDetails = findClientDetailsByClientId(clientId);
            }
        }

        app.setClientId(clientId);
        app.setClientSecret(RandomTools.generateRandomString(32));

        Cache cache;

        ConcurrentLruCache s;

        // 保存到数据库
        return CRUD.create(app) == null;
    }

    App findClientDetailsByClientId(String clientId) {
        return CRUD.info(App.class, "SELECT * FROM app WHERE stat = 1 AND client_id = ?", clientId);
    }
}
