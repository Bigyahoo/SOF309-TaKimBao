package com.vstay.servlet;

import com.vstay.dao.FavoriteDAO;
import com.vstay.dao.HotelDAO;
import com.vstay.model.Hotel;
import com.vstay.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link FavoriteServlet}.
 *
 * Tests: unauthenticated 401 response, missing hotelId 400,
 * add/remove toggle, hotel-not-found handling, DAO exceptions.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("FavoriteServlet — Unit Tests")
class FavoriteServletTest {

    @Mock private FavoriteDAO favoriteDAO;
    @Mock private HotelDAO hotelDAO;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private HttpSession session;

    private FavoriteServlet servlet;
    private StringWriter responseBody;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new FavoriteServlet();

        Field favField = FavoriteServlet.class.getDeclaredField("favoriteDAO");
        favField.setAccessible(true);
        favField.set(servlet, favoriteDAO);

        Field hotelField = FavoriteServlet.class.getDeclaredField("hotelDAO");
        hotelField.setAccessible(true);
        hotelField.set(servlet, hotelDAO);

        responseBody = new StringWriter();
        writer = new PrintWriter(responseBody);
        when(response.getWriter()).thenReturn(writer);
    }

    // =========================================================
    // Unauthenticated — 401
    // =========================================================

    @Test
    @DisplayName("doPost() — no session returns HTTP 401 with 'unauthorized' in JSON")
    void doPost_noSession_returns401() throws Exception {
        when(request.getSession(false)).thenReturn(null);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String body = responseBody.toString();
        assertTrue(body.contains("unauthorized"), "Response body must contain 'unauthorized'");
    }

    @Test
    @DisplayName("doPost() — session exists but no 'user' attribute returns HTTP 401")
    void doPost_sessionWithoutUser_returns401() throws Exception {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    // =========================================================
    // Missing / blank hotelId — 400
    // =========================================================

    @ParameterizedTest(name = "[{index}] hotelId=''{0}'' → 400 Bad Request")
    @CsvSource({
        "''",
        "'   '"  // Bao bọc bởi nháy đơn để bảo toàn khoảng trắng trong CSV
    })
    @DisplayName("doPost() — blank hotelId returns HTTP 400")
    void doPost_blankHotelId_returns400(String hotelId) throws Exception {
        User user = User.builder().id(1L).email("demo@vstay.vn").build();
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("hotelId")).thenReturn(hotelId);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("doPost() — null hotelId returns HTTP 400")
    void doPost_nullHotelId_returns400() throws Exception {
        User user = User.builder().id(1L).email("demo@vstay.vn").build();
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("hotelId")).thenReturn(null);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    // =========================================================
    // Toggle — ADD
    // =========================================================

    @Test
    @DisplayName("doPost() — hotel not yet favorited → adds favorite, returns 'added' status")
    void doPost_notFavorited_addsAndReturnsAdded() throws Exception {
        User user = User.builder().id(1L).email("demo@vstay.vn").build();
        Hotel hotel = new Hotel();
        hotel.setName("Grand Test Hotel");

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("hotelId")).thenReturn("5");
        when(favoriteDAO.isFavorite(1L, 5L)).thenReturn(false);
        when(hotelDAO.findById(5L)).thenReturn(hotel);

        servlet.doPost(request, response);

        verify(favoriteDAO).save(any());
        String body = responseBody.toString();
        assertTrue(body.contains("added"), "Response must contain 'added'");
    }

    // =========================================================
    // Toggle — REMOVE
    // =========================================================

    @Test
    @DisplayName("doPost() — hotel already favorited → removes favorite, returns 'removed' status")
    void doPost_alreadyFavorited_removesAndReturnsRemoved() throws Exception {
        User user = User.builder().id(1L).email("demo@vstay.vn").build();

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("hotelId")).thenReturn("5");
        when(favoriteDAO.isFavorite(1L, 5L)).thenReturn(true);

        servlet.doPost(request, response);

        verify(favoriteDAO).delete(1L, 5L);
        verify(favoriteDAO, never()).save(any());
        String body = responseBody.toString();
        assertTrue(body.contains("removed"), "Response must contain 'removed'");
    }

    // =========================================================
    // Hotel not found during add
    // =========================================================

    @Test
    @DisplayName("doPost() — hotelId valid but hotel not in DB → returns 'error' status")
    void doPost_hotelNotFound_returnsError() throws Exception {
        User user = User.builder().id(1L).email("demo@vstay.vn").build();

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("hotelId")).thenReturn("9999");
        when(favoriteDAO.isFavorite(1L, 9999L)).thenReturn(false);
        when(hotelDAO.findById(9999L)).thenReturn(null);

        servlet.doPost(request, response);

        verify(favoriteDAO, never()).save(any());
        String body = responseBody.toString();
        assertTrue(body.contains("error"), "Response must contain 'error'");
    }

    // =========================================================
    // Non-numeric hotelId — exception boundary
    // =========================================================

    @ParameterizedTest(name = "[{index}] non-numeric hotelId=''{0}'' → error JSON")
    @ValueSource(strings = { "abc", "!@#", "one", "1.5", "null" })
    @DisplayName("doPost() — non-numeric hotelId causes NumberFormatException, handled as error")
    void doPost_nonNumericHotelId_returnsError(String badId) throws Exception {
        User user = User.builder().id(1L).email("demo@vstay.vn").build();

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("hotelId")).thenReturn(badId);

        assertDoesNotThrow(() -> servlet.doPost(request, response));

        String body = responseBody.toString();
        assertTrue(body.contains("error"), "Response must contain 'error' for non-numeric id");
    }

    // =========================================================
    // Response Content-Type is JSON
    // =========================================================

    @Test
    @DisplayName("doPost() — Content-Type is set to application/json")
    void doPost_contentTypeIsJson() throws Exception {
        when(request.getSession(false)).thenReturn(null);

        servlet.doPost(request, response);

        verify(response).setContentType("application/json;charset=UTF-8");
    }

    // =========================================================
    // assertThrows — DAO throws unexpected exception
    // =========================================================

    @Test
    @DisplayName("doPost() — favoriteDAO.isFavorite() throws exception; handled gracefully with error JSON")
    void doPost_daoException_handledGracefully() throws Exception {
        User user = User.builder().id(1L).email("demo@vstay.vn").build();

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("hotelId")).thenReturn("5");
        when(favoriteDAO.isFavorite(anyLong(), anyLong()))
                .thenThrow(new RuntimeException("DB locked"));

        assertDoesNotThrow(() -> servlet.doPost(request, response));

        String body = responseBody.toString();
        assertTrue(body.contains("error"), "Exception must result in error JSON");
    }
}
