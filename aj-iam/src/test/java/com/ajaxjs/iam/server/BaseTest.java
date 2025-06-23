package com.ajaxjs.iam.server;

import com.ajaxjs.framework.spring.database.DataBaseConnection;
import com.ajaxjs.sqlman.JdbcConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
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
