package com.vstay.servlet;

import com.vstay.dao.BookingDAO;
import com.vstay.dao.FavoriteDAO;
import com.vstay.dao.UserDAO;
import com.vstay.model.Booking;
import com.vstay.model.Favorite;
import com.vstay.model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AccountServlet}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AccountServlet — Unit Tests")
class AccountServletTest {

    @Mock private UserDAO userDAO;
    @Mock private BookingDAO bookingDAO;
    @Mock private FavoriteDAO favoriteDAO;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private HttpSession session;
    @Mock private RequestDispatcher dispatcher;

    private AccountServlet servlet;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new AccountServlet();
        setField("userDAO", userDAO);
        setField("bookingDAO", bookingDAO);
        setField("favoriteDAO", favoriteDAO);
    }

    private void setField(String name, Object val) throws Exception {
        Field f = AccountServlet.class.getDeclaredField(name);
        f.setAccessible(true);
        f.set(servlet, val);
    }

    // =========================================================
    // Access control
    // =========================================================

    @Test
    @DisplayName("doGet() — no session redirects to /login")
    void doGet_noSession_redirectsToLogin() throws Exception {
        when(request.getSession(false)).thenReturn(null);
        when(request.getContextPath()).thenReturn("/vstay");

        servlet.doGet(request, response);

        verify(response).sendRedirect(contains("/login"));
    }

    @Test
    @DisplayName("doGet() — session without 'user' attribute redirects to /login")
    void doGet_sessionWithoutUser_redirectsToLogin() throws Exception {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);
        when(request.getContextPath()).thenReturn("/vstay");

        servlet.doGet(request, response);

        verify(response).sendRedirect(contains("/login"));
    }

    // =========================================================
    // User not found in DB (session stale)
    // =========================================================

    @Test
    @DisplayName("doGet() — user not in DB (stale session) invalidates session and redirects to /login")
    void doGet_userNotInDb_invalidatesAndRedirects() throws Exception {
        User staleUser = User.builder().id(99L).email("ghost@vstay.vn").build();
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(staleUser);
        when(userDAO.findById(99L)).thenReturn(null);
        when(request.getContextPath()).thenReturn("/vstay");

        servlet.doGet(request, response);

        verify(session).invalidate();
        verify(response).sendRedirect(contains("/login"));
    }

    // =========================================================
    // Valid account load
    // =========================================================

    @Test
    @DisplayName("doGet() — valid user forwards to account.jsp with bookings, favorites, user")
    void doGet_validUser_forwardsToAccountJsp() throws Exception {
        User sessionUser = User.builder().id(1L).email("demo@vstay.vn").role("USER").build();
        User freshUser = User.builder().id(1L).email("demo@vstay.vn").role("USER")
                .vPoints(500).vipTier("Silver").build();

        List<Booking> bookings = List.of(new Booking());
        List<Favorite> favorites = List.of(new Favorite());

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(sessionUser);
        when(userDAO.findById(1L)).thenReturn(freshUser);
        when(bookingDAO.findByUserId(1L)).thenReturn(bookings);
        when(favoriteDAO.findByUserId(1L)).thenReturn(favorites);
        when(request.getRequestDispatcher("/WEB-INF/views/account.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request).setAttribute("bookings", bookings);
        verify(request).setAttribute("favorites", favorites);
        verify(request).setAttribute("user", freshUser);
        verify(session).setAttribute("user", freshUser); // session refreshed
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doGet() — user with no bookings still loads account page with empty list")
    void doGet_noBookings_emptyList() throws Exception {
        User sessionUser = User.builder().id(2L).email("new@vstay.vn").build();
        User freshUser = User.builder().id(2L).email("new@vstay.vn").vPoints(100).build();

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(sessionUser);
        when(userDAO.findById(2L)).thenReturn(freshUser);
        when(bookingDAO.findByUserId(2L)).thenReturn(Collections.emptyList());
        when(favoriteDAO.findByUserId(2L)).thenReturn(Collections.emptyList());
        when(request.getRequestDispatcher("/WEB-INF/views/account.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request).setAttribute("bookings", Collections.emptyList());
        verify(request).setAttribute("favorites", Collections.emptyList());
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doGet() — fresh user data overwrites stale session user")
    void doGet_freshUserOverwritesSession() throws Exception {
        User sessionUser = User.builder().id(3L).vPoints(0).build();
        User freshUser = User.builder().id(3L).vPoints(5000).vipTier("Gold").build();

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(sessionUser);
        when(userDAO.findById(3L)).thenReturn(freshUser);
        when(bookingDAO.findByUserId(3L)).thenReturn(Collections.emptyList());
        when(favoriteDAO.findByUserId(3L)).thenReturn(Collections.emptyList());
        when(request.getRequestDispatcher("/WEB-INF/views/account.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        // Session must be updated with fresh user (5000 points, Gold tier)
        verify(session).setAttribute("user", freshUser);
        verify(request).setAttribute("user", freshUser);
    }
}
