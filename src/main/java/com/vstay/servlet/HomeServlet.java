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

@WebServlet(name = "HomeServlet", urlPatterns = {"", "/home", "/index"})
public class HomeServlet extends HttpServlet {

    private final HotelDAO hotelDAO = new HotelDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Hotel> featuredHotels = hotelDAO.findFeatured();
            if (featuredHotels == null || featuredHotels.isEmpty()) {
                featuredHotels = hotelDAO.findAll();
            }
            List<String> cities = hotelDAO.findDistinctCities();

            request.setAttribute("featuredHotels", featuredHotels);
            request.setAttribute("cities", cities);
            request.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(request, response);
        }
    }
}
