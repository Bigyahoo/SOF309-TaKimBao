package com.vstay.servlet;

import com.vstay.dao.FavoriteDAO;
import com.vstay.dao.HotelDAO;
import com.vstay.dao.ReviewDAO;
import com.vstay.model.Hotel;
import com.vstay.model.Review;
import com.vstay.model.Room;
import com.vstay.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "DetailsServlet", urlPatterns = {"/details"})
public class DetailsServlet extends HttpServlet {

    private final HotelDAO hotelDAO = new HotelDAO();
    private final ReviewDAO reviewDAO = new ReviewDAO();
    private final FavoriteDAO favoriteDAO = new FavoriteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/search");
            return;
        }

        try {
            Long id = Long.parseLong(idStr.trim());
            Hotel hotel = hotelDAO.findById(id);

            if (hotel == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy khách sạn.");
                return;
            }

            List<Review> reviews = reviewDAO.findByHotelId(id);
            List<Room> rooms = hotel.getRooms();

            boolean isFavorite = false;
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("user") != null) {
                User user = (User) session.getAttribute("user");
                isFavorite = favoriteDAO.isFavorite(user.getId(), id);
            }

            request.setAttribute("hotel", hotel);
            request.setAttribute("reviews", reviews);
            request.setAttribute("rooms", rooms);
            request.setAttribute("isFavorite", isFavorite);

            request.getRequestDispatcher("/WEB-INF/views/details.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/search");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
