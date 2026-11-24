package com.ajaxjs.iam.client;

import com.ajaxjs.iam.UserConstants;
import com.ajaxjs.iam.annotation.AllowOpenAccess;
import com.ajaxjs.iam.model.SimpleUser;
import com.ajaxjs.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

public class TestUserAccessHandlerInterceptor {
    private static final String USER_JSON = "{\"id\":42,\"name\":\"test-user\"}";

    private final UserInterceptor interceptor = new UserInterceptor();
    private final MockHttpServletRequest request = new MockHttpServletRequest();
    private final MockHttpServletResponse response = new MockHttpServletResponse();

    @BeforeEach
    public void setUp() {
        interceptor.setRun(true);
        interceptor.setCacheType("jvm_hash");
        interceptor.setGetUserFromJvmHash(token -> "valid-token".equals(token) ? USER_JSON : null);
    }

    @Test
    public void preHandle_ValidToken_ShouldSetUserAndContinue() throws Exception {
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer valid-token");

        SimpleUser user = new SimpleUser();
        user.setId(42L);
        user.setName("test-user");

        try (MockedStatic<JsonUtil> json = mockStatic(JsonUtil.class)) {
            json.when(() -> JsonUtil.fromJson(USER_JSON, SimpleUser.class)).thenReturn(user);

            assertTrue(interceptor.preHandle(request, response, handler("protectedEndpoint")));
            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertSame(user, request.getAttribute(UserConstants.USER_KEY_IN_REQUEST));
        }
    }

    @Test
    public void preHandle_MissingToken_ShouldReturnUnauthorized() throws Exception {
        assertFalse(interceptor.preHandle(request, response, handler("protectedEndpoint")));
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        MediaType contentType = MediaType.parseMediaType(response.getContentType());
        assertTrue(MediaType.APPLICATION_JSON.isCompatibleWith(contentType));
        assertEquals(StandardCharsets.UTF_8, contentType.getCharset());
        assertTrue(response.getContentAsString().contains("\"error\":\"unauthorized\""));
        assertTrue(response.getContentAsString().contains("未认证"));
        assertNull(request.getAttribute(UserConstants.USER_KEY_IN_REQUEST));
    }

    @Test
    public void preHandle_UnknownToken_ShouldReturnUnauthorized() throws Exception {
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer unknown-token");

        assertFalse(interceptor.preHandle(request, response, handler("protectedEndpoint")));
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        assertNull(request.getAttribute(UserConstants.USER_KEY_IN_REQUEST));
    }

    @Test
    public void preHandle_AllowOpenAccess_ShouldContinueWithoutToken() throws Exception {
        assertTrue(interceptor.preHandle(request, response, handler("openEndpoint")));
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNull(request.getAttribute(UserConstants.USER_KEY_IN_REQUEST));
    }

    private HandlerMethod handler(String methodName) throws NoSuchMethodException {
        TestController controller = new TestController();
        return new HandlerMethod(controller, TestController.class.getDeclaredMethod(methodName));
    }

    private static class TestController {
        public void protectedEndpoint() {
        }

        @AllowOpenAccess
        public void openEndpoint() {
        }
    }
}
