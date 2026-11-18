package com.poly.servlet;

import com.poly.dao.BookingDAO;
import com.poly.dao.FavoriteDAO;
import com.poly.dao.UserDAO;
import com.poly.model.Booking;
import com.poly.model.Favorite;
import com.poly.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AccountServlet", urlPatterns = {"/account"})
public class AccountServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final BookingDAO bookingDAO = new BookingDAO();
    private final FavoriteDAO favoriteDAO = new FavoriteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=" + request.getContextPath() + "/account");
            return;
        }

        User sessionUser = (User) session.getAttribute("user");
        User freshUser = userDAO.findById(sessionUser.getId());
        if (freshUser == null) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        session.setAttribute("user", freshUser);

        List<Booking> bookings = bookingDAO.findByUserId(freshUser.getId());
        List<Favorite> favorites = favoriteDAO.findByUserId(freshUser.getId());

        request.setAttribute("bookings", bookings);
        request.setAttribute("favorites", favorites);
        request.setAttribute("user", freshUser);

        request.getRequestDispatcher("/WEB-INF/views/account.jsp").forward(request, response);
    }
}
