package com.poly.servlet;

import com.poly.dao.HotelDAO;
import com.poly.model.Hotel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchServlet", urlPatterns = {"/search"})
public class SearchServlet extends HttpServlet {

    private final HotelDAO hotelDAO = new HotelDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String location = request.getParameter("location");
        String minPriceStr = request.getParameter("minPrice");
        String maxPriceStr = request.getParameter("maxPrice");
        String starsStr = request.getParameter("stars");
        String category = request.getParameter("category");
        String sort = request.getParameter("sort");
        String pageStr = request.getParameter("page");

        Integer minPrice = null;
        Integer maxPrice = null;
        Integer stars = null;
        int page = 1;
        int pageSize = 9;

        try {
            if (minPriceStr != null && !minPriceStr.trim().isEmpty()) {
                minPrice = Integer.parseInt(minPriceStr.trim());
            }
        } catch (NumberFormatException ignored) {}

        try {
            if (maxPriceStr != null && !maxPriceStr.trim().isEmpty()) {
                maxPrice = Integer.parseInt(maxPriceStr.trim());
            }
        } catch (NumberFormatException ignored) {}

        try {
            if (starsStr != null && !starsStr.trim().isEmpty()) {
                stars = Integer.parseInt(starsStr.trim());
            }
        } catch (NumberFormatException ignored) {}

        try {
            if (pageStr != null && !pageStr.trim().isEmpty()) {
                page = Math.max(1, Integer.parseInt(pageStr.trim()));
            }
        } catch (NumberFormatException ignored) {}

        try {
            List<Hotel> hotels = hotelDAO.search(location, minPrice, maxPrice, stars, category, sort, page, pageSize);
            long totalResults = hotelDAO.count(location, minPrice, maxPrice, stars, category);
            int totalPages = (int) Math.ceil((double) totalResults / pageSize);
            if (totalPages == 0) totalPages = 1;

            List<String> cities = hotelDAO.findDistinctCities();

            request.setAttribute("hotels", hotels);
            request.setAttribute("totalResults", totalResults);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("location", location);
            request.setAttribute("minPrice", minPrice);
            request.setAttribute("maxPrice", maxPrice);
            request.setAttribute("stars", stars);
            request.setAttribute("category", category);
            request.setAttribute("sort", sort != null ? sort : "recommended");
            request.setAttribute("cities", cities);

            request.getRequestDispatcher("/WEB-INF/views/search.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi tìm kiếm: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/search.jsp").forward(request, response);
        }
    }
}
