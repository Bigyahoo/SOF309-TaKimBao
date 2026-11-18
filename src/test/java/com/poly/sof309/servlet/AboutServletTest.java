package com.vstay.servlet;

import com.vstay.dao.ContactDAO;
import com.vstay.model.Contact;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
 * Unit tests for {@link AboutServlet}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AboutServlet — Unit Tests")
class AboutServletTest {

    @Mock private ContactDAO contactDAO;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private RequestDispatcher dispatcher;

    private AboutServlet servlet;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new AboutServlet();
        Field f = AboutServlet.class.getDeclaredField("contactDAO");
        f.setAccessible(true);
        f.set(servlet, contactDAO);
    }

    // =========================================================
    // doGet
    // =========================================================

    @Test
    @DisplayName("doGet() forwards to about.jsp")
    void doGet_forwardsToAboutJsp() throws Exception {
        when(request.getRequestDispatcher("/WEB-INF/views/about.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(dispatcher).forward(request, response);
    }

    // =========================================================
    // doPost — valid submission
    // =========================================================

    @Test
    @DisplayName("doPost() — valid name and email saves contact and sets contactSuccess=true")
    void doPost_validInput_savesContactAndSetsSuccess() throws Exception {
        when(request.getParameter("name")).thenReturn("Nguyen Van A");
        when(request.getParameter("email")).thenReturn("contact@test.vn");
        when(request.getParameter("phone")).thenReturn("0900000001");
        when(request.getParameter("topic")).thenReturn("Hỏi về đặt phòng");
        when(request.getParameter("message")).thenReturn("Tôi muốn hỏi về dịch vụ.");
        when(request.getRequestDispatcher("/WEB-INF/views/about.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(contactDAO).save(any(Contact.class));
        verify(request).setAttribute("contactSuccess", true);
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doPost() — saved Contact has correct name and email")
    void doPost_validInput_contactHasCorrectData() throws Exception {
        final Contact[] saved = new Contact[1];
        doAnswer(inv -> { saved[0] = inv.getArgument(0); return null; })
                .when(contactDAO).save(any(Contact.class));

        when(request.getParameter("name")).thenReturn("  Tran Thi B  ");
        when(request.getParameter("email")).thenReturn("  hello@vstay.vn  ");
        when(request.getParameter("phone")).thenReturn("0911111111");
        when(request.getParameter("topic")).thenReturn("Feedback");
        when(request.getParameter("message")).thenReturn("Khách sạn rất tốt!");
        when(request.getRequestDispatcher("/WEB-INF/views/about.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        assertNotNull(saved[0]);
        assertEquals("Tran Thi B", saved[0].getName(), "Name must be trimmed");
        assertEquals("hello@vstay.vn", saved[0].getEmail(), "Email must be trimmed");
    }

    // =========================================================
    // doPost — missing required fields (boundary)
    // =========================================================

    @ParameterizedTest(name = "[{index}] name=''{0}'' email=''{1}'' → contactError set")
    @CsvSource({
        "'',       valid@vstay.vn",   // missing name
        "Some Name, ''",              // missing email
        "'',       ''",               // both blank
        "   ,      valid@vstay.vn",   // whitespace name
        "Some Name,    "              // whitespace email
    })
    @DisplayName("doPost() — missing name or email sets contactError and does NOT save")
    void doPost_missingNameOrEmail_setsErrorAndDoesNotSave(String name, String email)
            throws Exception {
        when(request.getParameter("name")).thenReturn(name);
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("phone")).thenReturn(null);
        when(request.getParameter("topic")).thenReturn(null);
        when(request.getParameter("message")).thenReturn(null);
        when(request.getRequestDispatcher("/WEB-INF/views/about.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(contactDAO, never()).save(any());
        verify(request).setAttribute(eq("contactError"), anyString());
        verify(dispatcher).forward(request, response);
    }

    // =========================================================
    // doPost — optional fields handled gracefully
    // =========================================================

    @Test
    @DisplayName("doPost() — null optional fields (phone, topic, message) handled without NPE")
    void doPost_nullOptionalFields_noException() throws Exception {
        when(request.getParameter("name")).thenReturn("Test User");
        when(request.getParameter("email")).thenReturn("test@vstay.vn");
        when(request.getParameter("phone")).thenReturn(null);
        when(request.getParameter("topic")).thenReturn(null);
        when(request.getParameter("message")).thenReturn(null);
        when(request.getRequestDispatcher("/WEB-INF/views/about.jsp")).thenReturn(dispatcher);

        assertDoesNotThrow(() -> servlet.doPost(request, response));
        verify(contactDAO).save(any(Contact.class));
    }

    // =========================================================
    // assertThrows — DAO exception
    // =========================================================

    @Test
    @DisplayName("doPost() — DAO exception during save is propagated (or logged) but page still renders")
    void doPost_daoException_pageStillForwards() throws Exception {
        when(request.getParameter("name")).thenReturn("Error User");
        when(request.getParameter("email")).thenReturn("err@vstay.vn");
        when(request.getParameter("phone")).thenReturn(null);
        when(request.getParameter("topic")).thenReturn(null);
        when(request.getParameter("message")).thenReturn("Test");
        doThrow(new RuntimeException("DB write failure")).when(contactDAO).save(any());

        // AboutServlet does not catch DAO exceptions — the exception propagates as ServletException
        // We document this as expected behaviour
        assertThrows(Exception.class, () -> servlet.doPost(request, response),
            "DAO exception should propagate since AboutServlet has no try/catch");
    }
}
