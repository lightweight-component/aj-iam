package com.ajaxjs.iam.server;

import com.ajaxjs.framework.database.DataBaseConnection;
import com.ajaxjs.sqlman.JdbcConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = BaseConfig.class)
public abstract class BaseTest {
    @BeforeEach
    void initAll() {
        DataBaseConnection.initDb();
    }

    @AfterEach
    void closeDb() {
        JdbcConnection.closeDb();
    }
}
