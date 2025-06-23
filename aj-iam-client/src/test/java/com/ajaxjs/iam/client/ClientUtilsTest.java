package com.ajaxjs.iam.client;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ClientUtilsTest {
    @Mock
    private HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

    private StringWriter writer;

    @BeforeEach
    public void setUp() throws Exception {
        writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    public void testReturnForbidden() {
        ClientUtils.returnForbidden(response);
        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(response).setContentType("text/html;charset=UTF-8");
        assertEquals(writer.toString().trim(), "<html><body><h1>403 Forbidden</h1><p>非法操作，请求被拒绝。</p></body></html>");
    }
}
