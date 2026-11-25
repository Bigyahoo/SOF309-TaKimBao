package com.vstay.servlet;

import com.vstay.dao.BookingDAO;
import com.vstay.dao.HotelDAO;
import com.vstay.model.Hotel;
import com.vstay.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "AdminServlet", urlPatterns = {"/admin"})
public class AdminServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AdminServlet.class.getName());

    private final HotelDAO hotelDAO = new HotelDAO();
    private final BookingDAO bookingDAO = new BookingDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=" + request.getContextPath() + "/admin");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập trang quản trị.");
            return;
        }

        List<Hotel> hotels = hotelDAO.findAll();
        long totalBookings = bookingDAO.countAll();

        request.setAttribute("hotels", hotels);
        request.setAttribute("totalHotels", hotels.size());
        request.setAttribute("totalBookings", totalBookings);

        request.getRequestDispatcher("/WEB-INF/views/admin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Không có quyền thực hiện thao tác này.");
            return;
        }

        String action = request.getParameter("action");
        if ("delete".equalsIgnoreCase(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.trim().isEmpty()) {
                try {
                    hotelDAO.delete(Long.parseLong(idStr.trim()));
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Failed to delete hotel id=" + idStr, e);
                }
            }
        } else if ("create".equalsIgnoreCase(action) || "update".equalsIgnoreCase(action)) {
            try {
                String idStr = request.getParameter("id");
                String name = request.getParameter("name");
                String category = request.getParameter("category");
                String city = request.getParameter("city");
                String address = request.getParameter("address");
                String priceStr = request.getParameter("price");
                String oldPriceStr = request.getParameter("oldPrice");
                String starsStr = request.getParameter("stars");
                String ratingStr = request.getParameter("rating");
                String ratingText = request.getParameter("ratingText");
                String image = request.getParameter("image");
                String badge = request.getParameter("badge");
                String badgeSub = request.getParameter("badgeSub");
                String roomType = request.getParameter("roomType");
                String urgency = request.getParameter("urgency");
                String distance = request.getParameter("distance");
                String description = request.getParameter("description");
                String featuredStr = request.getParameter("featured");
                String inclusions = request.getParameter("inclusions");
                String amenities = request.getParameter("amenities");

                long price = priceStr != null && !priceStr.trim().isEmpty() ? Long.parseLong(priceStr.replaceAll("[^0-9]", "")) : 0L;
                Long oldPrice = oldPriceStr != null && !oldPriceStr.trim().isEmpty() ? Long.parseLong(oldPriceStr.replaceAll("[^0-9]", "")) : null;
                Integer stars = starsStr != null && !starsStr.trim().isEmpty() ? Integer.parseInt(starsStr.trim()) : 4;
                Double rating = ratingStr != null && !ratingStr.trim().isEmpty() ? Double.parseDouble(ratingStr.trim()) : 8.5;
                boolean featured = "true".equalsIgnoreCase(featuredStr) || "on".equalsIgnoreCase(featuredStr);

                int discountPercent = 0;
                if (oldPrice != null && oldPrice > price && price > 0) {
                    discountPercent = (int) Math.round(((double)(oldPrice - price) / oldPrice) * 100);
                }

                Hotel hotel;
                if ("update".equalsIgnoreCase(action) && idStr != null && !idStr.trim().isEmpty()) {
                    hotel = hotelDAO.findById(Long.parseLong(idStr.trim()));
                    if (hotel == null) {
                        hotel = new Hotel();
                    }
                } else {
                    hotel = new Hotel();
                }

                hotel.setName(name != null ? name.trim() : "Khách sạn mới");
                hotel.setCategory(category != null ? category.trim() : "Khách sạn");
                hotel.setCity(city != null ? city.trim() : "Đà Nẵng");
                hotel.setAddress(address != null ? address.trim() : "");
                hotel.setPrice(price);
                hotel.setOldPrice(oldPrice);
                hotel.setDiscountPercent(discountPercent);
                hotel.setStars(stars);
                hotel.setRating(rating);
                hotel.setRatingText(ratingText != null ? ratingText.trim() : "Tuyệt vời");
                hotel.setImage(image != null && !image.trim().isEmpty() ? image.trim() : "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=500");
                hotel.setBadge(badge);
                hotel.setBadgeSub(badgeSub);
                hotel.setRoomType(roomType);
                hotel.setUrgency(urgency);
                hotel.setDistance(distance);
                hotel.setDescription(description);
                hotel.setFeatured(featured);
                hotel.setInclusions(inclusions);
                hotel.setAmenities(amenities);

                if (hotel.getId() != null) {
                    hotelDAO.update(hotel);
                } else {
                    hotelDAO.save(hotel);
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to save hotel", e);
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin");
    }
}
