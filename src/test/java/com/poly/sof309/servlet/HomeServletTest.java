package com.vstay.servlet;

import com.vstay.dao.HotelDAO;
import com.vstay.model.Hotel;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link HomeServlet}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("HomeServlet — Unit Tests")
class HomeServletTest {

    @Mock private HotelDAO hotelDAO;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private RequestDispatcher dispatcher;

    private HomeServlet servlet;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new HomeServlet();
        Field f = HomeServlet.class.getDeclaredField("hotelDAO");
        f.setAccessible(true);
        f.set(servlet, hotelDAO);
    }

    @Test
    @DisplayName("doGet() — featured hotels are set as 'featuredHotels' attribute and forwarded to index.jsp")
    void doGet_featuredHotels_setAndForwarded() throws Exception {
        Hotel h1 = new Hotel(); h1.setName("Featured Hotel 1");
        Hotel h2 = new Hotel(); h2.setName("Featured Hotel 2");
        List<Hotel> featured = Arrays.asList(h1, h2);
        List<String> cities = List.of("Hà Nội", "Đà Nẵng");

        when(hotelDAO.findFeatured()).thenReturn(featured);
        when(hotelDAO.findDistinctCities()).thenReturn(cities);
        when(request.getRequestDispatcher("/WEB-INF/views/index.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request).setAttribute("featuredHotels", featured);
        verify(request).setAttribute("cities", cities);
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doGet() — when findFeatured() is empty, falls back to findAll()")
    void doGet_emptyFeatured_fallsBackToFindAll() throws Exception {
        Hotel h = new Hotel(); h.setName("Regular Hotel");
        List<Hotel> all = List.of(h);

        when(hotelDAO.findFeatured()).thenReturn(Collections.emptyList());
        when(hotelDAO.findAll()).thenReturn(all);
        when(hotelDAO.findDistinctCities()).thenReturn(Collections.emptyList());
        when(request.getRequestDispatcher("/WEB-INF/views/index.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(hotelDAO).findAll();
        verify(request).setAttribute("featuredHotels", all);
    }

    @Test
    @DisplayName("doGet() — DAO exception is caught; errorMessage set, still forwards to index.jsp")
    void doGet_daoException_handledGracefully() throws Exception {
        when(hotelDAO.findFeatured()).thenThrow(new RuntimeException("DB timeout"));
        when(request.getRequestDispatcher("/WEB-INF/views/index.jsp")).thenReturn(dispatcher);

        assertDoesNotThrow(() -> servlet.doGet(request, response));

        verify(request).setAttribute(eq("errorMessage"), anyString());
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doGet() — null findFeatured() triggers fallback to findAll()")
    void doGet_nullFeatured_fallsBackToFindAll() throws Exception {
        Hotel h = new Hotel(); h.setName("All Hotel");
        when(hotelDAO.findFeatured()).thenReturn(null);
        when(hotelDAO.findAll()).thenReturn(List.of(h));
        when(hotelDAO.findDistinctCities()).thenReturn(List.of());
        when(request.getRequestDispatcher("/WEB-INF/views/index.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(hotelDAO).findAll();
    }
}
