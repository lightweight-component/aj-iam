package com.ajaxjs.iam.user.service;

import com.ajaxjs.framework.FastCrudController.model.Namespaces;
import com.ajaxjs.iam.client.SecurityManager;
import com.ajaxjs.sqlman.crud.model.TableModel;
import com.ajaxjs.sqlman_v2.sqlgenerator.AutoQuery;
import com.ajaxjs.sqlman_v2.sqlgenerator.AutoQueryBusiness;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class UserCommonHttpApi extends Namespaces {
    {
        // TEST
        TableModel tableModel = new TableModel();
        tableModel.setTableName("user");
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
            public Serializable getCurrentUserId() {
                return SecurityManager.getUser().getId();
            }

            @Override
            public Serializable getTenantId() {
                return null;
            }
        };

        put("foo", new AutoQuery(tableModel, autoQueryBusiness));
    }
}
