package com.ajaxjs.iam.server.common;

import com.ajaxjs.framework.database.DataBaseConnection;
import com.ajaxjs.framework.dataservice.fastcrud.Namespaces;
import com.ajaxjs.iam.client.SecurityManager;
import com.ajaxjs.iam.user.service.TenantService;
import com.ajaxjs.sqlman.JdbcConnection;
import com.ajaxjs.sqlman.model.tablemodel.TableModel;
import com.ajaxjs.sqlman.sqlgenerator.AutoQuery;
import com.ajaxjs.sqlman.sqlgenerator.AutoQueryBusiness;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class IamFastCRUD extends Namespaces {
    {
        AutoQueryBusiness autoQueryBusiness = new AutoQueryBusiness() {
            @Override
            public boolean isListOrderByDate() {
                return true;
            }

            @Override
            public boolean isTenantIsolation() {
                return false;
            }

            @Override
            public boolean isCurrentUserOnly() {
                return false;
            }

            @Override
            public boolean isFilterDeleted() {
                return false;
            }

            @Override
            public Serializable getCurrentUserId() {
                return SecurityManager.getUser().getId();
            }

            @Override
            public Serializable getTenantId() {
                return null;
            }
        };

        TableModel accessTokenTableModel = new TableModel();
        accessTokenTableModel.setTableName("access_token");

        TableModel userTableModel = new TableModel();
        userTableModel.setTableName("user");

        TableModel userLoginLogTableModel = new TableModel();
        userLoginLogTableModel.setTableName("user_login_log");

        TableModel modulePermission = new TableModel();
        modulePermission.setTableName("ds_common_api");

//        put("access_token", new AutoQuery(accessTokenTableModel, autoQueryBusiness));
        put("user", new AutoQuery(userTableModel, autoQueryBusiness));
        put("user_login_log", new AutoQuery(userLoginLogTableModel, autoQueryBusiness));
        put("module_permission", new AutoQuery(modulePermission, autoQueryBusiness));
    }

    @EventListener
    public void loadConfig(ApplicationReadyEvent event) {
        DataBaseConnection.initDb();
        loadFromDB(() -> SecurityManager.getUser().getId(), TenantService::getTenantId);
        JdbcConnection.closeDb();
    }
}
