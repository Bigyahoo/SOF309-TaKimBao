package com.poly.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link LogoutServlet}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("LogoutServlet — Unit Tests")
class LogoutServletTest {

    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private HttpSession session;

    private LogoutServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new LogoutServlet();
    }

    @Test
    @DisplayName("doGet() — active session is invalidated")
    void doGet_activeSession_invalidated() throws Exception {
        when(request.getSession(false)).thenReturn(session);
        when(request.getContextPath()).thenReturn("/vstay");

        servlet.doGet(request, response);

        verify(session).invalidate();
        verify(response).sendRedirect("/vstay/");
    }

    @Test
    @DisplayName("doGet() — no session does not throw NullPointerException")
    void doGet_noSession_noNPE() throws Exception {
        when(request.getSession(false)).thenReturn(null);
        when(request.getContextPath()).thenReturn("/vstay");

        // Must complete without exception
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> servlet.doGet(request, response));
        verify(response).sendRedirect("/vstay/");
        verify(session, never()).invalidate();
    }

    @Test
    @DisplayName("doPost() delegates to doGet() — session is also invalidated")
    void doPost_delegatesToDoGet_sessionInvalidated() throws Exception {
        when(request.getSession(false)).thenReturn(session);
        when(request.getContextPath()).thenReturn("/vstay");

        servlet.doPost(request, response);

        verify(session).invalidate();
        verify(response).sendRedirect("/vstay/");
    }

    @Test
    @DisplayName("doGet() always redirects to / regardless of session state")
    void doGet_alwaysRedirectsToHome() throws Exception {
        when(request.getSession(false)).thenReturn(null);
        when(request.getContextPath()).thenReturn("");

        servlet.doGet(request, response);

        verify(response).sendRedirect("/");
    }
}
