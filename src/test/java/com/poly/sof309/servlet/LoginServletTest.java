package com.vstay.servlet;

import com.vstay.dao.UserDAO;
import com.vstay.model.User;
import com.vstay.util.PasswordUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link LoginServlet}.
 *
 * Uses Mockito to inject mock DAOs and mock Servlet API objects.
 * No running Tomcat or database is needed.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("LoginServlet — Unit Tests")
class LoginServletTest {

    @Mock private UserDAO userDAO;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private HttpSession session;
    @Mock private RequestDispatcher dispatcher;

    private LoginServlet servlet;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new LoginServlet();
        // Inject mock UserDAO via reflection (field is private final)
        Field daoField = LoginServlet.class.getDeclaredField("userDAO");
        daoField.setAccessible(true);
        daoField.set(servlet, userDAO);
    }

    // =========================================================
    // doGet — already-logged-in redirect
    // =========================================================

    @Test
    @DisplayName("doGet() — authenticated user is redirected to /")
    void doGet_alreadyLoggedIn_redirectsToHome() throws Exception {
        User loggedInUser = User.builder().id(1L).email("demo@vstay.vn").role("USER").build();
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(loggedInUser);
        when(request.getContextPath()).thenReturn("/vstay");

        servlet.doGet(request, response);

        verify(response).sendRedirect("/vstay/");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    @DisplayName("doGet() — guest (no session) is forwarded to login.jsp")
    void doGet_guest_forwardsToLoginJsp() throws Exception {
        when(request.getSession(false)).thenReturn(null);
        when(request.getRequestDispatcher("/WEB-INF/views/login.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(dispatcher).forward(request, response);
    }

    // =========================================================
    // doPost — empty / null input validation
    // =========================================================

    @ParameterizedTest(name = "[{index}] email=''{0}'' password=''{1}'' → validation error")
    @CsvSource({
        "'',       password123",
        "user@a.com, ''",
        "'',       ''",
        "   ,      password123",
        "user@a.com,    "
    })
    @DisplayName("doPost() — blank email or password sets error attribute and forwards to login.jsp")
    void doPost_blankCredentials_setsErrorAndForwards(String email, String password) throws Exception {
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getRequestDispatcher("/WEB-INF/views/login.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), anyString());
        verify(dispatcher).forward(request, response);
        verify(userDAO, never()).findByEmail(anyString());
    }

    @Test
    @DisplayName("doPost() — null email and null password sets error and forwards")
    void doPost_nullCredentials_setsErrorAndForwards() throws Exception {
        when(request.getParameter("email")).thenReturn(null);
        when(request.getParameter("password")).thenReturn(null);
        when(request.getRequestDispatcher("/WEB-INF/views/login.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), anyString());
        verify(dispatcher).forward(request, response);
    }

    // =========================================================
    // doPost — wrong credentials
    // =========================================================

    @ParameterizedTest(name = "[{index}] login attempt with email=''{0}''")
    @CsvSource({
        "wrong@vstay.vn, wrongpass",
        "admin@vstay.vn, wrongpass",
        "demo@vstay.vn,  DEMO123"
    })
    @DisplayName("doPost() — wrong password sets error attribute (user not found or hash mismatch)")
    void doPost_wrongCredentials_setsError(String email, String password) throws Exception {
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("redirect")).thenReturn(null);
        // DAO returns null → user not found
        when(userDAO.findByEmail(email.trim())).thenReturn(null);
        when(request.getRequestDispatcher("/WEB-INF/views/login.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), anyString());
        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    // =========================================================
    // doPost — successful login (USER)
    // =========================================================

    @Test
    @DisplayName("doPost() — correct credentials for USER redirects to /")
    void doPost_correctCredentials_user_redirectsToHome() throws Exception {
        String salt = PasswordUtil.generateSalt();
        String hash = PasswordUtil.hashPassword("demo123", salt);
        User user = User.builder().id(2L).email("demo@vstay.vn")
                .salt(salt).passwordHash(hash).role("USER").build();

        when(request.getParameter("email")).thenReturn("demo@vstay.vn");
        when(request.getParameter("password")).thenReturn("demo123");
        when(request.getParameter("redirect")).thenReturn(null);
        when(userDAO.findByEmail("demo@vstay.vn")).thenReturn(user);
        when(request.getSession(true)).thenReturn(session);
        when(request.getContextPath()).thenReturn("/vstay");

        servlet.doPost(request, response);

        verify(session).setAttribute("user", user);
        verify(response).sendRedirect("/vstay/");
    }

    // =========================================================
    // doPost — successful login (ADMIN)
    // =========================================================

    @Test
    @DisplayName("doPost() — correct credentials for ADMIN redirects to /admin")
    void doPost_correctCredentials_admin_redirectsToAdmin() throws Exception {
        String salt = PasswordUtil.generateSalt();
        String hash = PasswordUtil.hashPassword("admin123", salt);
        User admin = User.builder().id(1L).email("admin@vstay.vn")
                .salt(salt).passwordHash(hash).role("ADMIN").build();

        when(request.getParameter("email")).thenReturn("admin@vstay.vn");
        when(request.getParameter("password")).thenReturn("admin123");
        when(request.getParameter("redirect")).thenReturn(null);
        when(userDAO.findByEmail("admin@vstay.vn")).thenReturn(admin);
        when(request.getSession(true)).thenReturn(session);
        when(request.getContextPath()).thenReturn("/vstay");

        servlet.doPost(request, response);

        verify(session).setAttribute("user", admin);
        verify(response).sendRedirect("/vstay/admin");
    }

    // =========================================================
    // doPost — custom redirect parameter
    // =========================================================

    @Test
    @DisplayName("doPost() — safe redirect parameter is honoured after successful login")
    void doPost_safeRedirect_honoured() throws Exception {
        String salt = PasswordUtil.generateSalt();
        String hash = PasswordUtil.hashPassword("demo123", salt);
        User user = User.builder().id(2L).email("demo@vstay.vn")
                .salt(salt).passwordHash(hash).role("USER").build();

        when(request.getParameter("email")).thenReturn("demo@vstay.vn");
        when(request.getParameter("password")).thenReturn("demo123");
        when(request.getParameter("redirect")).thenReturn("/vstay/checkout?hotelId=5");
        when(userDAO.findByEmail("demo@vstay.vn")).thenReturn(user);
        when(request.getSession(true)).thenReturn(session);

        servlet.doPost(request, response);

        verify(response).sendRedirect("/vstay/checkout?hotelId=5");
    }

    @Test
    @DisplayName("doPost() — open-redirect with '://' in redirect param is blocked; falls back to /")
    void doPost_openRedirectBlocked() throws Exception {
        String salt = PasswordUtil.generateSalt();
        String hash = PasswordUtil.hashPassword("demo123", salt);
        User user = User.builder().id(2L).email("demo@vstay.vn")
                .salt(salt).passwordHash(hash).role("USER").build();

        when(request.getParameter("email")).thenReturn("demo@vstay.vn");
        when(request.getParameter("password")).thenReturn("demo123");
        when(request.getParameter("redirect")).thenReturn("https://evil.com");
        when(userDAO.findByEmail("demo@vstay.vn")).thenReturn(user);
        when(request.getSession(true)).thenReturn(session);
        when(request.getContextPath()).thenReturn("/vstay");

        servlet.doPost(request, response);

        // Must NOT redirect to evil.com
        verify(response, never()).sendRedirect("https://evil.com");
        // Must redirect to safe home
        verify(response).sendRedirect("/vstay/");
    }

    // =========================================================
    // assertThrows — DAO throws unexpected exception
    // =========================================================

    @Test
    @DisplayName("doPost() — DAO exception is caught; error attribute set, page forwarded")
    void doPost_daoThrowsException_handledGracefully() throws Exception {
        when(request.getParameter("email")).thenReturn("demo@vstay.vn");
        when(request.getParameter("password")).thenReturn("demo123");
        when(request.getParameter("redirect")).thenReturn(null);
        when(userDAO.findByEmail(anyString()))
                .thenThrow(new RuntimeException("DB connection failed"));
        when(request.getRequestDispatcher("/WEB-INF/views/login.jsp")).thenReturn(dispatcher);

        // Must not propagate exception out of doPost
        assertDoesNotThrow(() -> servlet.doPost(request, response));

        verify(request).setAttribute(eq("error"), anyString());
        verify(dispatcher).forward(request, response);
    }
}
