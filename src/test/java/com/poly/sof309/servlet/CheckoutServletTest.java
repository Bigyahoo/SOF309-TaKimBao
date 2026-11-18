package com.poly.servlet;

import com.poly.dao.BookingDAO;
import com.poly.dao.HotelDAO;
import com.poly.dao.UserDAO;
import com.poly.model.Booking;
import com.poly.model.Hotel;
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
 * Unit tests for {@link CheckoutServlet}.
 *
 * Validates: success-page GET, missing hotel redirect, booking creation,
 * V-Points calculation, payment method handling, and date/nights parsing.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CheckoutServlet — Unit Tests")
class CheckoutServletTest {

    @Mock private HotelDAO hotelDAO;
    @Mock private BookingDAO bookingDAO;
    @Mock private UserDAO userDAO;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private HttpSession session;
    @Mock private RequestDispatcher dispatcher;

    private CheckoutServlet servlet;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new CheckoutServlet();
        injectField("hotelDAO", hotelDAO);
        injectField("bookingDAO", bookingDAO);
        injectField("userDAO", userDAO);
    }

    private void injectField(String name, Object value) throws Exception {
        Field f = CheckoutServlet.class.getDeclaredField(name);
        f.setAccessible(true);
        f.set(servlet, value);
    }

    // =========================================================
    // doGet — success page
    // =========================================================

    @Test
    @DisplayName("doGet() with success=true and code forwards to checkout.jsp with booking")
    void doGet_successPage_loadsBooking() throws Exception {
        Booking booking = Booking.builder().bookingCode("VS123456").build();
        when(request.getParameter("success")).thenReturn("true");
        when(request.getParameter("code")).thenReturn("VS123456");
        when(bookingDAO.findByBookingCode("VS123456")).thenReturn(booking);
        when(request.getRequestDispatcher("/WEB-INF/views/checkout.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request).setAttribute("booking", booking);
        verify(request).setAttribute("success", true);
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doGet() with no hotel id redirects to /search")
    void doGet_noHotelId_redirectsToSearch() throws Exception {
        when(request.getParameter("success")).thenReturn(null);
        when(request.getParameter("id")).thenReturn(null);
        when(request.getContextPath()).thenReturn("/vstay");

        servlet.doGet(request, response);

        verify(response).sendRedirect("/vstay/search");
    }

    @ParameterizedTest(name = "[{index}] non-numeric/blank id=''{0}'' → redirect to /search")
    @CsvSource({ "abc", "null", "!@#", "9999999999999999999" })
    @DisplayName("doGet() — invalid hotel id redirects to /search")
    void doGet_invalidHotelId_redirectsToSearch(String badId) throws Exception {
        when(request.getParameter("success")).thenReturn(null);
        when(request.getParameter("id")).thenReturn(badId);
        when(request.getContextPath()).thenReturn("/vstay");

        servlet.doGet(request, response);

        verify(response).sendRedirect("/vstay/search");
    }

    @Test
    @DisplayName("doGet() — hotel not found in DB redirects to /search")
    void doGet_hotelNotFound_redirectsToSearch() throws Exception {
        when(request.getParameter("success")).thenReturn(null);
        when(request.getParameter("id")).thenReturn("999");
        when(hotelDAO.findById(999L)).thenReturn(null);
        when(request.getContextPath()).thenReturn("/vstay");

        servlet.doGet(request, response);

        verify(response).sendRedirect("/vstay/search");
    }

    @Test
    @DisplayName("doGet() — valid hotel id forwards to checkout.jsp with hotel attribute")
    void doGet_validHotelId_forwardsToCheckout() throws Exception {
        Hotel hotel = new Hotel();
        hotel.setName("Grand Hotel");
        when(request.getParameter("success")).thenReturn(null);
        when(request.getParameter("id")).thenReturn("5");
        when(hotelDAO.findById(5L)).thenReturn(hotel);
        when(request.getParameter("room")).thenReturn("Deluxe");
        when(request.getParameter("price")).thenReturn("1500000");
        when(request.getParameter("checkIn")).thenReturn(null);
        when(request.getParameter("checkOut")).thenReturn(null);
        when(request.getRequestDispatcher("/WEB-INF/views/checkout.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request).setAttribute("hotel", hotel);
        verify(dispatcher).forward(request, response);
    }

    // =========================================================
    // doPost — booking creation
    // =========================================================

    private void stubPostParams(String hotelId, String nights, String totalPrice,
                                String checkIn, String checkOut, String payment) {
        when(request.getParameter("hotelId")).thenReturn(hotelId);
        when(request.getParameter("nights")).thenReturn(nights);
        when(request.getParameter("totalPrice")).thenReturn(totalPrice);
        when(request.getParameter("checkIn")).thenReturn(checkIn);
        when(request.getParameter("checkOut")).thenReturn(checkOut);
        when(request.getParameter("paymentMethod")).thenReturn(payment);
        when(request.getParameter("guestName")).thenReturn("Nguyen Van A");
        when(request.getParameter("guestEmail")).thenReturn("guest@vstay.vn");
        when(request.getParameter("guestPhone")).thenReturn("0900000000");
        when(request.getParameter("roomType")).thenReturn("Deluxe");
        when(request.getParameterValues("specialRequests")).thenReturn(null);
        when(request.getParameter("specialNotes")).thenReturn("");
    }

    @Test
    @DisplayName("doPost() — saves booking and redirects to /checkout?success=true&code=...")
    void doPost_validBooking_savesAndRedirects() throws Exception {
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setPrice(1_000_000L);

        stubPostParams("1", "2", "2000000",
                "2027-01-10", "2027-01-12", "momo");

        when(hotelDAO.findById(1L)).thenReturn(hotel);
        when(request.getSession(false)).thenReturn(null); // guest booking
        when(request.getContextPath()).thenReturn("/vstay");

        servlet.doPost(request, response);

        verify(bookingDAO).save(any(Booking.class));
        verify(response).sendRedirect(startsWith("/vstay/checkout?success=true&code="));
    }

    @ParameterizedTest(name = "[{index}] nights={0}, price={1} → V-Points = price/10000")
    @CsvSource({
        "1, 1000000, 100",
        "2, 2000000, 200",
        "3, 3500000, 350",
        "5, 5000000, 500"
    })
    @DisplayName("doPost() — V-Points earned = totalPrice / 10000 (boundary check)")
    void doPost_vPointsCalculation(String nights, String price, int expectedPoints) throws Exception {
        Hotel hotel = new Hotel();
        hotel.setName("VPoint Hotel");
        hotel.setPrice(Long.parseLong(price) / Long.parseLong(nights));

        User user = User.builder().id(1L).email("demo@vstay.vn").role("USER").vPoints(0).build();

        stubPostParams("1", nights, price, "2027-02-01", "2027-02-0" + (1 + Integer.parseInt(nights)), "vnpay");
        when(hotelDAO.findById(1L)).thenReturn(hotel);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getContextPath()).thenReturn("/vstay");

        final Booking[] savedBooking = new Booking[1];
        doAnswer(inv -> { savedBooking[0] = inv.getArgument(0); return null; })
                .when(bookingDAO).save(any(Booking.class));

        servlet.doPost(request, response);

        assertNotNull(savedBooking[0]);
        assertEquals(expectedPoints, savedBooking[0].getVPointsEarned(),
            "V-Points earned must equal totalPrice / 10000");
    }

    @Test
    @DisplayName("doPost() — booking code starts with 'VS' prefix")
    void doPost_bookingCode_hasCorrectPrefix() throws Exception {
        Hotel hotel = new Hotel();
        hotel.setName("Code Hotel");
        hotel.setPrice(500_000L);

        stubPostParams("1", "1", "500000", "2027-03-01", "2027-03-02", "cash");
        when(hotelDAO.findById(1L)).thenReturn(hotel);
        when(request.getSession(false)).thenReturn(null);
        when(request.getContextPath()).thenReturn("/vstay");

        final Booking[] saved = new Booking[1];
        doAnswer(inv -> { saved[0] = inv.getArgument(0); return null; })
                .when(bookingDAO).save(any(Booking.class));

        servlet.doPost(request, response);

        assertNotNull(saved[0].getBookingCode());
        assertTrue(saved[0].getBookingCode().startsWith("VS"),
            "Booking code must start with 'VS'");
    }

    @ParameterizedTest(name = "[{index}] payment method ''{0}'' is stored as-is")
    @CsvSource({ "momo", "vnpay", "zalopay", "cash" })
    @DisplayName("doPost() — payment method is correctly stored in the booking")
    void doPost_paymentMethod_storedCorrectly(String method) throws Exception {
        Hotel hotel = new Hotel();
        hotel.setName("Pay Hotel");
        hotel.setPrice(800_000L);

        stubPostParams("1", "1", "800000", "2027-04-01", "2027-04-02", method);
        when(hotelDAO.findById(1L)).thenReturn(hotel);
        when(request.getSession(false)).thenReturn(null);
        when(request.getContextPath()).thenReturn("/vstay");

        final Booking[] saved = new Booking[1];
        doAnswer(inv -> { saved[0] = inv.getArgument(0); return null; })
                .when(bookingDAO).save(any(Booking.class));

        servlet.doPost(request, response);

        assertEquals(method, saved[0].getPaymentMethod());
    }

    // =========================================================
    // assertThrows — DAO exception during booking save
    // =========================================================

    @Test
    @DisplayName("doPost() — DAO exception during save is caught; error forwarded to checkout.jsp")
    void doPost_daoException_handledGracefully() throws Exception {
        Hotel hotel = new Hotel();
        hotel.setName("Broken Hotel");
        hotel.setPrice(500_000L);

        stubPostParams("1", "1", "500000", "2027-05-01", "2027-05-02", "momo");
        when(hotelDAO.findById(1L)).thenReturn(hotel);
        when(request.getSession(false)).thenReturn(null);
        doThrow(new RuntimeException("DB write failure")).when(bookingDAO).save(any());
        when(request.getRequestDispatcher("/WEB-INF/views/checkout.jsp")).thenReturn(dispatcher);

        assertDoesNotThrow(() -> servlet.doPost(request, response));

        verify(request).setAttribute(eq("error"), anyString());
        verify(dispatcher).forward(request, response);
    }
}
