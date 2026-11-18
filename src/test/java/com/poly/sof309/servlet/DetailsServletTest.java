package com.poly.servlet;

import com.poly.dao.FavoriteDAO;
import com.poly.dao.HotelDAO;
import com.poly.dao.ReviewDAO;
import com.poly.model.Hotel;
import com.poly.model.Review;
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
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link DetailsServlet}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DetailsServlet — Unit Tests")
class DetailsServletTest {

    @Mock private HotelDAO hotelDAO;
    @Mock private ReviewDAO reviewDAO;
    @Mock private FavoriteDAO favoriteDAO;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private HttpSession session;
    @Mock private RequestDispatcher dispatcher;

    private DetailsServlet servlet;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new DetailsServlet();
        setField("hotelDAO", hotelDAO);
        setField("reviewDAO", reviewDAO);
        setField("favoriteDAO", favoriteDAO);
    }

    private void setField(String name, Object value) throws Exception {
        Field f = DetailsServlet.class.getDeclaredField(name);
        f.setAccessible(true);
        f.set(servlet, value);
    }

    // =========================================================
    // Missing / invalid id
    // =========================================================

    @Test
    @DisplayName("doGet() — null id redirects to /search")
    void doGet_nullId_redirectsToSearch() throws Exception {
        when(request.getParameter("id")).thenReturn(null);
        when(request.getContextPath()).thenReturn("/vstay");

        servlet.doGet(request, response);

        verify(response).sendRedirect("/vstay/search");
    }

    @Test
    @DisplayName("doGet() — blank id redirects to /search")
    void doGet_blankId_redirectsToSearch() throws Exception {
        when(request.getParameter("id")).thenReturn("  ");
        when(request.getContextPath()).thenReturn("/vstay");

        servlet.doGet(request, response);

        verify(response).sendRedirect("/vstay/search");
    }

    @ParameterizedTest(name = "[{index}] non-numeric id=''{0}'' → redirect to /search")
    @ValueSource(strings = { "abc", "!@#", "1.5", "NaN" })
    @DisplayName("doGet() — non-numeric id (NumberFormatException) redirects to /search")
    void doGet_nonNumericId_redirectsToSearch(String badId) throws Exception {
        when(request.getParameter("id")).thenReturn(badId);
        when(request.getContextPath()).thenReturn("/vstay");

        servlet.doGet(request, response);

        verify(response).sendRedirect("/vstay/search");
        verify(hotelDAO, never()).findById(anyLong());
    }

    // =========================================================
    // Hotel not found
    // =========================================================

    @Test
    @DisplayName("doGet() — hotel not found sends 404 error")
    void doGet_hotelNotFound_sends404() throws Exception {
        when(request.getParameter("id")).thenReturn("9999");
        when(hotelDAO.findById(9999L)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendError(eq(HttpServletResponse.SC_NOT_FOUND), anyString());
    }

    // =========================================================
    // Valid hotel — forwarded to details.jsp
    // =========================================================

    @Test
    @DisplayName("doGet() — valid hotel id forwards to details.jsp with hotel, reviews, rooms")
    void doGet_validHotel_forwardsToDetails() throws Exception {
        Hotel hotel = new Hotel();
        hotel.setName("Grand Test Hotel");
        hotel.setRooms(Collections.emptyList());

        List<Review> reviews = List.of(new Review(), new Review());

        when(request.getParameter("id")).thenReturn("1");
        when(hotelDAO.findById(1L)).thenReturn(hotel);
        when(reviewDAO.findByHotelId(1L)).thenReturn(reviews);
        when(request.getSession(false)).thenReturn(null);
        when(request.getRequestDispatcher("/WEB-INF/views/details.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request).setAttribute("hotel", hotel);
        verify(request).setAttribute("reviews", reviews);
        verify(request).setAttribute("isFavorite", false);
        verify(dispatcher).forward(request, response);
    }

    // =========================================================
    // Favorite status check — authenticated user
    // =========================================================

    @Test
    @DisplayName("doGet() — authenticated user with hotel favorited sets isFavorite=true")
    void doGet_authenticatedUser_isFavoriteTrue() throws Exception {
        Hotel hotel = new Hotel();
        hotel.setRooms(Collections.emptyList());
        User user = User.builder().id(5L).email("demo@vstay.vn").build();

        when(request.getParameter("id")).thenReturn("3");
        when(hotelDAO.findById(3L)).thenReturn(hotel);
        when(reviewDAO.findByHotelId(3L)).thenReturn(Collections.emptyList());
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(favoriteDAO.isFavorite(5L, 3L)).thenReturn(true);
        when(request.getRequestDispatcher("/WEB-INF/views/details.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request).setAttribute("isFavorite", true);
    }

    @Test
    @DisplayName("doGet() — authenticated user with hotel NOT favorited sets isFavorite=false")
    void doGet_authenticatedUser_isFavoriteFalse() throws Exception {
        Hotel hotel = new Hotel();
        hotel.setRooms(Collections.emptyList());
        User user = User.builder().id(5L).email("demo@vstay.vn").build();

        when(request.getParameter("id")).thenReturn("3");
        when(hotelDAO.findById(3L)).thenReturn(hotel);
        when(reviewDAO.findByHotelId(3L)).thenReturn(Collections.emptyList());
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(favoriteDAO.isFavorite(5L, 3L)).thenReturn(false);
        when(request.getRequestDispatcher("/WEB-INF/views/details.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request).setAttribute("isFavorite", false);
    }

    // =========================================================
    // assertThrows — unexpected DAO exception
    // =========================================================

    @Test
    @DisplayName("doGet() — DAO exception sends HTTP 500 and does not propagate")
    void doGet_daoException_sends500() throws Exception {
        when(request.getParameter("id")).thenReturn("1");
        when(hotelDAO.findById(1L)).thenThrow(new RuntimeException("DB error"));

        assertDoesNotThrow(() -> servlet.doGet(request, response));

        verify(response).sendError(eq(HttpServletResponse.SC_INTERNAL_SERVER_ERROR), anyString());
    }
}
