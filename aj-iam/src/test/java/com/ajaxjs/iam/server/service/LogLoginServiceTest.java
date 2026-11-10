package com.ajaxjs.iam.server.service;

import com.ajaxjs.iam.server.config.LoginLogConfig;
import com.ajaxjs.iam.server.model.LogLogin;
import com.ajaxjs.iam.server.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class LogLoginServiceTest {
    @Test
    void loginReturnsWhileWorkerIsBlockedAndUsesCapturedData() throws Exception {
        ThreadPoolTaskExecutor executor = new LoginLogConfig().loginLogExecutor();
        executor.initialize();
        CountDownLatch started = new CountDownLatch(1);
        CountDownLatch release = new CountDownLatch(1);
        CountDownLatch finished = new CountDownLatch(1);
        AtomicReference<LogLogin> saved = new AtomicReference<>();
        AtomicReference<Thread> worker = new AtomicReference<>();
        LogLoginService service = new LogLoginService() {
            @Override
            void saveLoginLogAsync(LogLogin bean) {
                worker.set(Thread.currentThread());
                started.countDown();
                try {
                    if (release.await(5, TimeUnit.SECONDS))
                        saved.set(bean);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    finished.countDown();
                }
            }
        };
        ReflectionTestUtils.setField(service, "executor", executor);
        User user = new User();
        user.setId(123L);
        user.setLoginId("alice");
        user.setTenantId(9L);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");
        request.addHeader("user-agent", "test-agent");

        try {
            assertTimeout(Duration.ofSeconds(1), () -> service.saveLoginLog(user, request));
            assertTrue(started.await(2, TimeUnit.SECONDS));
            assertNotSame(Thread.currentThread(), worker.get());
            assertNull(saved.get());
            user.setLoginId("changed");
            user.setTenantId(10L);
            request.setRemoteAddr("192.0.2.1");
            release.countDown();
            assertTrue(finished.await(2, TimeUnit.SECONDS));
            assertEquals(123L, saved.get().getUserId());
            assertEquals("alice", saved.get().getUserName());
            assertEquals(9, saved.get().getTenantId());
            assertEquals("127.0.0.1", saved.get().getIp());
            assertEquals("test-agent", saved.get().getUserAgent());
        } finally {
            release.countDown();
            executor.shutdown();
        }
    }

    @Test
    void rejectedTaskDoesNotFailLogin() {
        ThreadPoolTaskExecutor executor = new LoginLogConfig().loginLogExecutor();
        executor.initialize();
        executor.shutdown();
        LogLoginService service = new LogLoginService();
        ReflectionTestUtils.setField(service, "executor", executor);

        assertDoesNotThrow(() -> service.saveLoginLog(new User(), null));
    }
}
