package com.poly.servlet;

import com.poly.dao.UserDAO;
import com.poly.model.User;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link RegisterServlet}.
 *
 * Tests form validation, duplicate-email detection, and successful registration.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RegisterServlet — Unit Tests")
class RegisterServletTest {

    @Mock private UserDAO userDAO;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private HttpSession session;
    @Mock private RequestDispatcher dispatcher;

    private RegisterServlet servlet;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new RegisterServlet();
        Field daoField = RegisterServlet.class.getDeclaredField("userDAO");
        daoField.setAccessible(true);
        daoField.set(servlet, userDAO);
    }

    // =========================================================
    // doGet — redirects to login?tab=register
    // =========================================================

    @Test
    @DisplayName("doGet() redirects to /login?tab=register")
    void doGet_redirectsToLogin() throws Exception {
        when(request.getContextPath()).thenReturn("/vstay");
        servlet.doGet(request, response);
        verify(response).sendRedirect("/vstay/login?tab=register");
    }

    // =========================================================
    // doPost — missing required fields (boundary)
    // =========================================================

    @ParameterizedTest(name = "[{index}] fullName=''{0}'' email=''{1}'' password=''{2}'' → validation error")
    @CsvSource({
        "'',       valid@email.com, pass123, pass123",   // missing fullName
        "Nguyen A, '',             pass123, pass123",   // missing email
        "Nguyen A, valid@email.com, '',     ''",        // missing password
        "'',       '',             '',      ''",        // all blank
        "   ,      valid@email.com, pass123, pass123"   // whitespace fullName
    })
    @DisplayName("doPost() — missing required fields sets registerError and forwards to login.jsp")
    void doPost_missingRequiredFields_setsError(
            String fullName, String email, String password, String confirm) throws Exception {

        when(request.getParameter("fullName")).thenReturn(fullName);
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("confirmPassword")).thenReturn(confirm);
        when(request.getParameter("phone")).thenReturn("0900000000");
        when(request.getRequestDispatcher("/WEB-INF/views/login.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("registerError"), anyString());
        verify(dispatcher).forward(request, response);
        verify(userDAO, never()).findByEmail(anyString());
    }

    // =========================================================
    // doPost — password mismatch (boundary)
    // =========================================================

    @ParameterizedTest(name = "[{index}] password=''{0}'' confirm=''{1}'' → mismatch error")
    @CsvSource({
        "password123, PASSWORD123",
        "pass@2024,   pass@2025",
        "abc,         abcd",
        "secret,      secre"
    })
    @DisplayName("doPost() — mismatched passwords sets registerError about password mismatch")
    void doPost_passwordMismatch_setsError(String password, String confirm) throws Exception {
        when(request.getParameter("fullName")).thenReturn("Test User");
        when(request.getParameter("email")).thenReturn("test@vstay.vn");
        when(request.getParameter("phone")).thenReturn("");
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("confirmPassword")).thenReturn(confirm);
        when(request.getRequestDispatcher("/WEB-INF/views/login.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("registerError"), contains("khớp"));
        verify(dispatcher).forward(request, response);
        verify(userDAO, never()).findByEmail(anyString());
    }

    // =========================================================
    // doPost — duplicate email
    // =========================================================

    @Test
    @DisplayName("doPost() — existing email sets registerError about duplicate email")
    void doPost_duplicateEmail_setsError() throws Exception {
        User existing = User.builder().id(1L).email("demo@vstay.vn").build();
        when(request.getParameter("fullName")).thenReturn("Demo User");
        when(request.getParameter("email")).thenReturn("demo@vstay.vn");
        when(request.getParameter("phone")).thenReturn("0900000000");
        when(request.getParameter("password")).thenReturn("demo123");
        when(request.getParameter("confirmPassword")).thenReturn("demo123");
        when(userDAO.findByEmail("demo@vstay.vn")).thenReturn(existing);
        when(request.getRequestDispatcher("/WEB-INF/views/login.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("registerError"), contains("đăng ký"));
        verify(dispatcher).forward(request, response);
        verify(userDAO, never()).save(any());
    }

    // =========================================================
    // doPost — successful registration
    // =========================================================

    @Test
    @DisplayName("doPost() — valid new user is saved and session is set, then redirected to /")
    void doPost_validNewUser_savesAndRedirects() throws Exception {
        when(request.getParameter("fullName")).thenReturn("Nguyen Van A");
        when(request.getParameter("email")).thenReturn("newuser@vstay.vn");
        when(request.getParameter("phone")).thenReturn("0912345678");
        when(request.getParameter("password")).thenReturn("MyPass@2024");
        when(request.getParameter("confirmPassword")).thenReturn("MyPass@2024");
        when(userDAO.findByEmail("newuser@vstay.vn")).thenReturn(null);
        when(request.getSession(true)).thenReturn(session);
        when(request.getContextPath()).thenReturn("/vstay");

        servlet.doPost(request, response);

        // User must be persisted
        verify(userDAO).save(any(User.class));
        // Session must carry the new user
        verify(session).setAttribute(eq("user"), any(User.class));
        // Redirect to home
        verify(response).sendRedirect("/vstay/");
    }

    @Test
    @DisplayName("doPost() — registered user has role USER and welcome V-Points")
    void doPost_newUser_hasCorrectRoleAndPoints() throws Exception {
        // Capture the saved user
        final User[] saved = new User[1];
        doAnswer(inv -> { saved[0] = inv.getArgument(0); return null; })
                .when(userDAO).save(any(User.class));

        when(request.getParameter("fullName")).thenReturn("Test User");
        when(request.getParameter("email")).thenReturn("brand@vstay.vn");
        when(request.getParameter("phone")).thenReturn("");
        when(request.getParameter("password")).thenReturn("pass123");
        when(request.getParameter("confirmPassword")).thenReturn("pass123");
        when(userDAO.findByEmail("brand@vstay.vn")).thenReturn(null);
        when(request.getSession(true)).thenReturn(session);
        when(request.getContextPath()).thenReturn("/vstay");

        servlet.doPost(request, response);

        assertNotNull(saved[0]);
        assertEquals("USER", saved[0].getRole());
        assertTrue(saved[0].getVPoints() > 0, "Welcome bonus V-Points must be > 0");
        assertNotNull(saved[0].getSalt(), "Salt must be generated");
        assertNotNull(saved[0].getPasswordHash(), "Password hash must be set");
        assertNotEquals("pass123", saved[0].getPasswordHash(), "Password must be hashed, not plain");
    }

    // =========================================================
    // assertThrows — DAO exception
    // =========================================================

    @Test
    @DisplayName("doPost() — DAO exception during save is caught; registerError set, forwards to login.jsp")
    void doPost_daoException_handledGracefully() throws Exception {
        when(request.getParameter("fullName")).thenReturn("Test User");
        when(request.getParameter("email")).thenReturn("ex@vstay.vn");
        when(request.getParameter("phone")).thenReturn("");
        when(request.getParameter("password")).thenReturn("pass123");
        when(request.getParameter("confirmPassword")).thenReturn("pass123");
        when(userDAO.findByEmail("ex@vstay.vn")).thenReturn(null);
        doThrow(new RuntimeException("DB timeout")).when(userDAO).save(any());
        when(request.getRequestDispatcher("/WEB-INF/views/login.jsp")).thenReturn(dispatcher);

        assertDoesNotThrow(() -> servlet.doPost(request, response));

        verify(request).setAttribute(eq("registerError"), anyString());
        verify(dispatcher).forward(request, response);
    }
}
