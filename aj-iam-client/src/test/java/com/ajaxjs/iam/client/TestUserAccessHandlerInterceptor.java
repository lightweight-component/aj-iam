package com.ajaxjs.iam.client;

import com.ajaxjs.iam.client.filter.UserAccessHandlerInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class TestUserAccessHandlerInterceptor {
    private final UserAccessHandlerInterceptor interceptor = new UserAccessHandlerInterceptor();

    private final HandlerMethod handlerMethod = Mockito.mock(HandlerMethod.class);

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private HttpSession session;

    @BeforeEach
    public void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        session = request.getSession();
        assert session != null;
        request.setSession(session);
    }

    @Test
    public void preHandle_UserLoggedIn_ShouldContinue() throws Exception {
        when(handlerMethod.getMethod()).thenReturn(getMethodWithAnnotation(NeedsUserLogined.class));
        session.setAttribute(UserAccessHandlerInterceptor.USER_IN_SESSION, "user");

        boolean result = interceptor.preHandle(request, response, handlerMethod);

        assertTrue(result);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void preHandle_UserNotLoggedIn_ShouldReturnUnauthorized() throws Exception {
        when(handlerMethod.getMethod()).thenReturn(getMethodWithAnnotation(NeedsUserLogined.class));

        boolean result = interceptor.preHandle(request, response, handlerMethod);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        assertEquals("text/plain; charset=UTF-8", response.getContentType());
        assertTrue(response.getContentAsString().contains("未登录，请先进行登录"));
        assertFalse(result);
    }

    @Test
    public void preHandle_NoAnnotation_ShouldContinue() throws Exception {
        when(handlerMethod.getMethod()).thenReturn(getMethodWithoutAnnotation());

        boolean result = interceptor.preHandle(request, response, handlerMethod);

        assertTrue(result);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    private Method getMethodWithAnnotation(Class<? extends java.lang.annotation.Annotation> annotation) throws NoSuchMethodException {
        // A method that is annotated with @NeedsUserLogined, used for testing
        return getClass().getDeclaredMethod("annotatedMethod", annotation);
    }

    private Method getMethodWithoutAnnotation() throws NoSuchMethodException {
        // A method that has no annotations, used for testing
        return getClass().getDeclaredMethod("nonAnnotatedMethod");
    }

    @NeedsUserLogined
    public void annotatedMethod(NeedsUserLogined annotation) {
        // Dummy method for testing
    }

    public void nonAnnotatedMethod() {
        // Dummy method for testing
    }
}
