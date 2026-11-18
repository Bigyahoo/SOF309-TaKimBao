package com.vstay.servlet;

import com.vstay.dao.BookingDAO;
import com.vstay.dao.HotelDAO;
import com.vstay.model.Hotel;
import com.vstay.model.User;
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
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AdminServlet}.
 *
 * Tests: access control (no session, USER role, ADMIN role),
 * CRUD actions (delete, create, update), discount calculation.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AdminServlet — Unit Tests")
class AdminServletTest {

    @Mock private HotelDAO hotelDAO;
    @Mock private BookingDAO bookingDAO;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private HttpSession session;
    @Mock private RequestDispatcher dispatcher;

    private AdminServlet servlet;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new AdminServlet();
        Field h = AdminServlet.class.getDeclaredField("hotelDAO");
        h.setAccessible(true); h.set(servlet, hotelDAO);
        Field b = AdminServlet.class.getDeclaredField("bookingDAO");
        b.setAccessible(true); b.set(servlet, bookingDAO);
    }

    // =========================================================
    // doGet — access control
    // =========================================================

    @Test
    @DisplayName("doGet() — no session redirects to /login")
    void doGet_noSession_redirectsToLogin() throws Exception {
        when(request.getSession(false)).thenReturn(null);
        when(request.getContextPath()).thenReturn("/vstay");

        servlet.doGet(request, response);

        verify(response).sendRedirect(contains("/login"));
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    @DisplayName("doGet() — USER role sends 403 Forbidden")
    void doGet_userRole_sends403() throws Exception {
        User userRole = User.builder().id(2L).role("USER").build();
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(userRole);

        servlet.doGet(request, response);

        verify(response).sendError(eq(HttpServletResponse.SC_FORBIDDEN), anyString());
    }

    @Test
    @DisplayName("doGet() — ADMIN role loads hotel list and forwards to admin.jsp")
    void doGet_adminRole_forwardsToAdminJsp() throws Exception {
        User admin = User.builder().id(1L).role("ADMIN").build();
        List<Hotel> hotels = Arrays.asList(new Hotel(), new Hotel());

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(admin);
        when(hotelDAO.findAll()).thenReturn(hotels);
        when(bookingDAO.countAll()).thenReturn(10L);
        when(request.getRequestDispatcher("/WEB-INF/views/admin.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request).setAttribute("hotels", hotels);
        verify(request).setAttribute("totalHotels", 2);
        verify(request).setAttribute("totalBookings", 10L);
        verify(dispatcher).forward(request, response);
    }

    // =========================================================
    // doPost — access control
    // =========================================================

    @Test
    @DisplayName("doPost() — no session redirects to /login")
    void doPost_noSession_redirectsToLogin() throws Exception {
        when(request.getSession(false)).thenReturn(null);
        when(request.getContextPath()).thenReturn("/vstay");

        servlet.doPost(request, response);

        verify(response).sendRedirect(contains("/login"));
    }

    @Test
    @DisplayName("doPost() — USER role sends 403 Forbidden")
    void doPost_userRole_sends403() throws Exception {
        User userRole = User.builder().id(2L).role("USER").build();
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(userRole);

        servlet.doPost(request, response);

        verify(response).sendError(eq(HttpServletResponse.SC_FORBIDDEN), anyString());
    }

    // =========================================================
    // doPost — delete action
    // =========================================================

    @Test
    @DisplayName("doPost() action=delete — calls hotelDAO.delete() with correct id")
    void doPost_deleteAction_callsDelete() throws Exception {
        User admin = User.builder().id(1L).role("ADMIN").build();
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(admin);
        when(request.getParameter("action")).thenReturn("delete");
        when(request.getParameter("id")).thenReturn("42");
        when(request.getContextPath()).thenReturn("/vstay");

        servlet.doPost(request, response);

        verify(hotelDAO).delete(42L);
        verify(response).sendRedirect(contains("/admin"));
    }

    @Test
    @DisplayName("doPost() action=delete — blank id is ignored gracefully")
    void doPost_deleteAction_blankId_ignored() throws Exception {
        User admin = User.builder().id(1L).role("ADMIN").build();
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(admin);
        when(request.getParameter("action")).thenReturn("delete");
        when(request.getParameter("id")).thenReturn("   ");
        when(request.getContextPath()).thenReturn("/vstay");

        assertDoesNotThrow(() -> servlet.doPost(request, response));
        verify(hotelDAO, never()).delete(anyLong());
    }

    // =========================================================
    // doPost — create action
    // =========================================================

    private void stubAdminCreateParams(String name, String price, String oldPrice,
                                       String stars, String city) {
        when(request.getParameter("action")).thenReturn("create");
        when(request.getParameter("id")).thenReturn(null);
        when(request.getParameter("name")).thenReturn(name);
        when(request.getParameter("price")).thenReturn(price);
        when(request.getParameter("oldPrice")).thenReturn(oldPrice);
        when(request.getParameter("stars")).thenReturn(stars);
        when(request.getParameter("city")).thenReturn(city);
        when(request.getParameter("category")).thenReturn("resort");
        when(request.getParameter("address")).thenReturn("123 Test St");
        when(request.getParameter("rating")).thenReturn("8.5");
        when(request.getParameter("ratingText")).thenReturn("Tuyệt vời");
        when(request.getParameter("image")).thenReturn("");
        when(request.getParameter("badge")).thenReturn(null);
        when(request.getParameter("badgeSub")).thenReturn(null);
        when(request.getParameter("roomType")).thenReturn(null);
        when(request.getParameter("urgency")).thenReturn(null);
        when(request.getParameter("distance")).thenReturn(null);
        when(request.getParameter("description")).thenReturn("Test description");
        when(request.getParameter("featured")).thenReturn("false");
        when(request.getParameter("inclusions")).thenReturn(null);
        when(request.getParameter("amenities")).thenReturn("WiFi, Pool");
    }

    @Test
    @DisplayName("doPost() action=create — valid hotel is saved via hotelDAO.save()")
    void doPost_createAction_savesHotel() throws Exception {
        User admin = User.builder().id(1L).role("ADMIN").build();
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(admin);
        when(request.getContextPath()).thenReturn("/vstay");
        stubAdminCreateParams("New Hotel", "1500000", "2000000", "5", "Hà Nội");

        servlet.doPost(request, response);

        verify(hotelDAO).save(any(Hotel.class));
        verify(response).sendRedirect(contains("/admin"));
    }

    @ParameterizedTest(name = "[{index}] price={0}, oldPrice={1} → discountPercent={2}%")
    @CsvSource({
        "1000000, 2000000, 50",   // 50% off
        "1500000, 2000000, 25",   // 25% off
        "1800000, 2000000, 10",   // 10% off
        "2000000, 2000000, 0",    // no discount (equal prices)
        "2000000, 1000000, 0"     // new price > old → no discount (data error guard)
    })
    @DisplayName("doPost() action=create — discount percentage calculated correctly at boundaries")
    void doPost_createAction_discountCalculated(String price, String oldPrice, int expectedDiscount)
            throws Exception {
        User admin = User.builder().id(1L).role("ADMIN").build();
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(admin);
        when(request.getContextPath()).thenReturn("/vstay");
        stubAdminCreateParams("Discount Hotel", price, oldPrice, "4", "Đà Nẵng");

        final Hotel[] saved = new Hotel[1];
        doAnswer(inv -> { saved[0] = inv.getArgument(0); return null; })
                .when(hotelDAO).save(any(Hotel.class));

        servlet.doPost(request, response);

        assertNotNull(saved[0], "Hotel must be saved");
        assertEquals(expectedDiscount, saved[0].getDiscountPercent(),
            "Discount percent must be calculated correctly");
    }

    // =========================================================
    // doPost — update action
    // =========================================================

    @Test
    @DisplayName("doPost() action=update — existing hotel is updated via hotelDAO.update()")
    void doPost_updateAction_updatesHotel() throws Exception {
        Hotel existing = new Hotel();
        existing.setId(5L);
        existing.setName("Old Name");

        User admin = User.builder().id(1L).role("ADMIN").build();
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(admin);
        when(request.getContextPath()).thenReturn("/vstay");
        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("id")).thenReturn("5");
        when(request.getParameter("name")).thenReturn("Updated Name");
        when(request.getParameter("price")).thenReturn("1200000");
        when(request.getParameter("oldPrice")).thenReturn("");
        when(request.getParameter("stars")).thenReturn("4");
        when(request.getParameter("city")).thenReturn("Hội An");
        when(request.getParameter("category")).thenReturn("boutique");
        when(request.getParameter("address")).thenReturn("456 Updated Rd");
        when(request.getParameter("rating")).thenReturn("9.0");
        when(request.getParameter("ratingText")).thenReturn("Xuất sắc");
        when(request.getParameter("image")).thenReturn("https://example.com/img.jpg");
        when(request.getParameter("badge")).thenReturn(null);
        when(request.getParameter("badgeSub")).thenReturn(null);
        when(request.getParameter("roomType")).thenReturn(null);
        when(request.getParameter("urgency")).thenReturn(null);
        when(request.getParameter("distance")).thenReturn(null);
        when(request.getParameter("description")).thenReturn("Updated desc");
        when(request.getParameter("featured")).thenReturn("true");
        when(request.getParameter("inclusions")).thenReturn(null);
        when(request.getParameter("amenities")).thenReturn("Gym");
        when(hotelDAO.findById(5L)).thenReturn(existing);

        servlet.doPost(request, response);

        verify(hotelDAO).update(existing);
        assertEquals("Updated Name", existing.getName());
        assertEquals(Boolean.TRUE, existing.getFeatured(), "featured=true must be reflected");
    }

    // =========================================================
    // assertThrows — DAO exception during delete
    // =========================================================

    @Test
    @DisplayName("doPost() action=delete — DAO exception during delete is caught gracefully")
    void doPost_deleteAction_daoException_handledGracefully() throws Exception {
        User admin = User.builder().id(1L).role("ADMIN").build();
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(admin);
        when(request.getParameter("action")).thenReturn("delete");
        when(request.getParameter("id")).thenReturn("7");
        when(request.getContextPath()).thenReturn("/vstay");
        doThrow(new RuntimeException("FK constraint violation")).when(hotelDAO).delete(7L);

        // Must not propagate
        assertDoesNotThrow(() -> servlet.doPost(request, response));
        verify(response).sendRedirect(contains("/admin"));
    }
}
