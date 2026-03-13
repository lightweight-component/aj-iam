//package com.ajaxjs.iam.server.service;
//
//import com.ajaxjs.dataservice.core.DataAccessObject;
//import com.ajaxjs.dataservice.core.DataService;
//import com.ajaxjs.dataservice.crud.CrudService;
//import com.ajaxjs.framework.database.DataBaseConnection;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Service;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//
//@Service
//public class CommonApiService extends DataService implements DataServiceController {
//    @Autowired
//    private CrudService crudService;
//
//    @Override
//    public DataAccessObject getDao() {
//        return crudService;
//    }
//
//    @EventListener
//    public void handleContextRefresh(ContextRefreshedEvent event) throws SQLException {
//        try (Connection conn = DataBaseConnection.initDb()) {
//            setDao(crudService);
//            reloadConfig();// 在 Spring 初始化完成后执行的操作
//        }
//    }
//}