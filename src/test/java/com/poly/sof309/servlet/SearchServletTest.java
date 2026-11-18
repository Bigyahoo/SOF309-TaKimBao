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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link SearchServlet}.
 *
 * Validates parameter parsing, attribute population, pagination calculation,
 * boundary values on price/stars/page, and error handling.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SearchServlet — Unit Tests")
class SearchServletTest {

    @Mock private HotelDAO hotelDAO;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private RequestDispatcher dispatcher;

    private SearchServlet servlet;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new SearchServlet();
        Field f = SearchServlet.class.getDeclaredField("hotelDAO");
        f.setAccessible(true);
        f.set(servlet, hotelDAO);
    }

    private void stubSearchCall(List<Hotel> hotels, long total) throws Exception {
        when(hotelDAO.search(any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(hotels);
        when(hotelDAO.count(any(), any(), any(), any(), any())).thenReturn(total);
        when(hotelDAO.findDistinctCities()).thenReturn(List.of("Hà Nội", "Đà Nẵng"));
        when(request.getRequestDispatcher("/WEB-INF/views/search.jsp")).thenReturn(dispatcher);
    }

    // =========================================================
    // Basic search — results forwarded
    // =========================================================

    @Test
    @DisplayName("doGet() — search result list is set as request attribute 'hotels'")
    void doGet_resultsSetAsAttribute() throws Exception {
        Hotel h1 = new Hotel(); h1.setName("Hotel Alpha");
        Hotel h2 = new Hotel(); h2.setName("Hotel Beta");
        List<Hotel> hotels = Arrays.asList(h1, h2);

        when(request.getParameter("location")).thenReturn("Hà Nội");
        when(request.getParameter("minPrice")).thenReturn(null);
        when(request.getParameter("maxPrice")).thenReturn(null);
        when(request.getParameter("stars")).thenReturn(null);
        when(request.getParameter("category")).thenReturn(null);
        when(request.getParameter("sort")).thenReturn(null);
        when(request.getParameter("page")).thenReturn(null);
        stubSearchCall(hotels, 2);

        servlet.doGet(request, response);

        verify(request).setAttribute("hotels", hotels);
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doGet() — empty results sets empty list, totalResults=0")
    void doGet_noResults_setsEmptyList() throws Exception {
        when(request.getParameter("location")).thenReturn("ZZZ_NOT_FOUND");
        when(request.getParameter("minPrice")).thenReturn(null);
        when(request.getParameter("maxPrice")).thenReturn(null);
        when(request.getParameter("stars")).thenReturn(null);
        when(request.getParameter("category")).thenReturn(null);
        when(request.getParameter("sort")).thenReturn(null);
        when(request.getParameter("page")).thenReturn(null);
        stubSearchCall(Collections.emptyList(), 0);

        servlet.doGet(request, response);

        verify(request).setAttribute("hotels", Collections.emptyList());
        verify(request).setAttribute("totalResults", 0L);
    }

    // =========================================================
    // Pagination calculation (boundary)
    // =========================================================

    @ParameterizedTest(name = "[{index}] totalResults={0}, expectedPages={1}")
    @CsvSource({
        "0,  1",   // zero results → at least 1 page
        "1,  1",   // 1 result → 1 page
        "9,  1",   // exactly one page
        "10, 2",   // 9+1 results → 2 pages
        "18, 2",   // exactly 2 pages
        "19, 3",   // 18+1 → 3 pages
        "27, 3",   // exactly 3 pages
        "28, 4"    // 27+1 → 4 pages
    })
    @DisplayName("doGet() — totalPages calculation at boundary values (pageSize=9)")
    void doGet_totalPagesCalculation(long totalResults, int expectedPages) throws Exception {
        when(request.getParameter("location")).thenReturn(null);
        when(request.getParameter("minPrice")).thenReturn(null);
        when(request.getParameter("maxPrice")).thenReturn(null);
        when(request.getParameter("stars")).thenReturn(null);
        when(request.getParameter("category")).thenReturn(null);
        when(request.getParameter("sort")).thenReturn(null);
        when(request.getParameter("page")).thenReturn(null);
        stubSearchCall(Collections.emptyList(), totalResults);

        servlet.doGet(request, response);

        verify(request).setAttribute("totalPages", expectedPages);
    }

    // =========================================================
    // Price / stars / page parameter parsing (boundary)
    // =========================================================

    @ParameterizedTest(name = "[{index}] minPrice=''{0}'' → parsed as {1}")
    @CsvSource({
        "0,          0",
        "500000,     500000",
        "1000000,    1000000",
        "9999999,    9999999"
    })
    @DisplayName("doGet() — minPrice parameter is correctly parsed to Integer")
    void doGet_minPriceParsed(String input, int expected) throws Exception {
        when(request.getParameter("location")).thenReturn(null);
        when(request.getParameter("minPrice")).thenReturn(input);
        when(request.getParameter("maxPrice")).thenReturn(null);
        when(request.getParameter("stars")).thenReturn(null);
        when(request.getParameter("category")).thenReturn(null);
        when(request.getParameter("sort")).thenReturn(null);
        when(request.getParameter("page")).thenReturn(null);
        stubSearchCall(Collections.emptyList(), 0);

        servlet.doGet(request, response);

        verify(request).setAttribute("minPrice", expected);
    }

    @ParameterizedTest(name = "[{index}] stars=''{0}'' → parsed as {1}")
    @CsvSource({
        "1, 1",
        "2, 2",
        "3, 3",
        "4, 4",
        "5, 5"
    })
    @DisplayName("doGet() — stars parameter boundary values (1–5) are correctly parsed")
    void doGet_starsParsed(String input, int expected) throws Exception {
        when(request.getParameter("location")).thenReturn(null);
        when(request.getParameter("minPrice")).thenReturn(null);
        when(request.getParameter("maxPrice")).thenReturn(null);
        when(request.getParameter("stars")).thenReturn(input);
        when(request.getParameter("category")).thenReturn(null);
        when(request.getParameter("sort")).thenReturn(null);
        when(request.getParameter("page")).thenReturn(null);
        stubSearchCall(Collections.emptyList(), 0);

        servlet.doGet(request, response);

        verify(request).setAttribute("stars", expected);
    }

    @ParameterizedTest(name = "[{index}] non-numeric param ''{0}'' → ignored (null attribute)")
    @ValueSource(strings = { "abc", "!@#", "one", "--", "1.5" })
    @DisplayName("doGet() — non-numeric minPrice/stars/page values are silently ignored (null)")
    void doGet_nonNumericParams_ignored(String bad) throws Exception {
        when(request.getParameter("location")).thenReturn(null);
        when(request.getParameter("minPrice")).thenReturn(bad);
        when(request.getParameter("maxPrice")).thenReturn(null);
        when(request.getParameter("stars")).thenReturn(null);
        when(request.getParameter("category")).thenReturn(null);
        when(request.getParameter("sort")).thenReturn(null);
        when(request.getParameter("page")).thenReturn(null);
        stubSearchCall(Collections.emptyList(), 0);

        // Must not throw
        assertDoesNotThrow(() -> servlet.doGet(request, response));

        // minPrice attribute must be null (parsing failed silently)
        verify(request).setAttribute("minPrice", (Integer) null);
    }

    @ParameterizedTest(name = "[{index}] page=''{0}'' → clamped to max(1, value)")
    @CsvSource({
        "0,  1",   // 0 → clamped to 1
        "-1, 1",   // negative → clamped to 1
        "1,  1",
        "2,  2",
        "10, 10"
    })
    @DisplayName("doGet() — page number is clamped to minimum 1")
    void doGet_pageClamped(String pageInput, int expectedPage) throws Exception {
        when(request.getParameter("location")).thenReturn(null);
        when(request.getParameter("minPrice")).thenReturn(null);
        when(request.getParameter("maxPrice")).thenReturn(null);
        when(request.getParameter("stars")).thenReturn(null);
        when(request.getParameter("category")).thenReturn(null);
        when(request.getParameter("sort")).thenReturn(null);
        when(request.getParameter("page")).thenReturn(pageInput);
        stubSearchCall(Collections.emptyList(), 0);

        servlet.doGet(request, response);

        verify(request).setAttribute("currentPage", expectedPage);
    }

    // =========================================================
    // Sort parameter passthrough
    // =========================================================

    @ParameterizedTest(name = "[{index}] sort=''{0}'' → attribute set as ''{0}''")
    @CsvSource({
        "price_asc,  price_asc",
        "price_desc, price_desc",
        "rating,     rating",
        "discount,   discount"
    })
    @DisplayName("doGet() — sort parameter is passed through to request attribute")
    void doGet_sortAttributeSet(String sortIn, String sortOut) throws Exception {
        when(request.getParameter("location")).thenReturn(null);
        when(request.getParameter("minPrice")).thenReturn(null);
        when(request.getParameter("maxPrice")).thenReturn(null);
        when(request.getParameter("stars")).thenReturn(null);
        when(request.getParameter("category")).thenReturn(null);
        when(request.getParameter("sort")).thenReturn(sortIn);
        when(request.getParameter("page")).thenReturn(null);
        stubSearchCall(Collections.emptyList(), 0);

        servlet.doGet(request, response);

        verify(request).setAttribute("sort", sortOut);
    }

    @Test
    @DisplayName("doGet() — null sort defaults to 'recommended'")
    void doGet_nullSort_defaultsToRecommended() throws Exception {
        when(request.getParameter("location")).thenReturn(null);
        when(request.getParameter("minPrice")).thenReturn(null);
        when(request.getParameter("maxPrice")).thenReturn(null);
        when(request.getParameter("stars")).thenReturn(null);
        when(request.getParameter("category")).thenReturn(null);
        when(request.getParameter("sort")).thenReturn(null);
        when(request.getParameter("page")).thenReturn(null);
        stubSearchCall(Collections.emptyList(), 0);

        servlet.doGet(request, response);

        verify(request).setAttribute("sort", "recommended");
    }

    // =========================================================
    // assertThrows — DAO exception
    // =========================================================

    @Test
    @DisplayName("doGet() — DAO exception is caught; errorMessage set, forwards to search.jsp")
    void doGet_daoException_handledGracefully() throws Exception {
        when(request.getParameter("location")).thenReturn("Crash City");
        when(request.getParameter("minPrice")).thenReturn(null);
        when(request.getParameter("maxPrice")).thenReturn(null);
        when(request.getParameter("stars")).thenReturn(null);
        when(request.getParameter("category")).thenReturn(null);
        when(request.getParameter("sort")).thenReturn(null);
        when(request.getParameter("page")).thenReturn(null);

        when(hotelDAO.search(any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
                .thenThrow(new RuntimeException("DB crashed"));
        when(request.getRequestDispatcher("/WEB-INF/views/search.jsp")).thenReturn(dispatcher);

        assertDoesNotThrow(() -> servlet.doGet(request, response));

        verify(request).setAttribute(eq("errorMessage"), anyString());
        verify(dispatcher).forward(request, response);
    }
}
