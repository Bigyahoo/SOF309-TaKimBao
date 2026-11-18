package com.poly.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.dao.FavoriteDAO;
import com.poly.dao.HotelDAO;
import com.poly.model.Favorite;
import com.poly.model.Hotel;
import com.poly.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "FavoriteServlet", urlPatterns = {"/favorite"})
public class FavoriteServlet extends HttpServlet {

    private final FavoriteDAO favoriteDAO = new FavoriteDAO();
    private final HotelDAO hotelDAO = new HotelDAO();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession(false);
        Map<String, Object> jsonResponse = new HashMap<>();

        if (session == null || session.getAttribute("user") == null) {
            jsonResponse.put("status", "unauthorized");
            jsonResponse.put("message", "Vui lòng đăng nhập để lưu khách sạn yêu thích.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(objectMapper.writeValueAsString(jsonResponse));
            return;
        }

        User user = (User) session.getAttribute("user");
        String hotelIdStr = request.getParameter("hotelId");

        if (hotelIdStr == null || hotelIdStr.trim().isEmpty()) {
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Thiếu hotelId");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(objectMapper.writeValueAsString(jsonResponse));
            return;
        }

        try {
            Long hotelId = Long.parseLong(hotelIdStr.trim());
            boolean isFav = favoriteDAO.isFavorite(user.getId(), hotelId);

            if (isFav) {
                favoriteDAO.delete(user.getId(), hotelId);
                jsonResponse.put("status", "removed");
                jsonResponse.put("message", "Đã xóa khỏi danh sách yêu thích");
            } else {
                Hotel hotel = hotelDAO.findById(hotelId);
                if (hotel != null) {
                    Favorite favorite = Favorite.builder()
                            .user(user)
                            .hotel(hotel)
                            .build();
                    favoriteDAO.save(favorite);
                    jsonResponse.put("status", "added");
                    jsonResponse.put("message", "Đã lưu vào danh sách yêu thích");
                } else {
                    jsonResponse.put("status", "error");
                    jsonResponse.put("message", "Không tìm thấy khách sạn");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.put("status", "error");
            jsonResponse.put("message", e.getMessage());
        }

        response.getWriter().write(objectMapper.writeValueAsString(jsonResponse));
    }
}
