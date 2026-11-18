package com.poly.servlet;

import com.poly.dao.BookingDAO;
import com.poly.dao.HotelDAO;
import com.poly.dao.UserDAO;
import com.poly.model.Booking;
import com.poly.model.Hotel;
import com.poly.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {

    private final HotelDAO hotelDAO = new HotelDAO();
    private final BookingDAO bookingDAO = new BookingDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String isSuccess = request.getParameter("success");
        String code = request.getParameter("code");

        if ("true".equals(isSuccess) && code != null) {
            Booking booking = bookingDAO.findByBookingCode(code);
            request.setAttribute("booking", booking);
            request.setAttribute("success", true);
            request.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(request, response);
            return;
        }

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/search");
            return;
        }

        try {
            Long hotelId = Long.parseLong(idStr.trim());
            Hotel hotel = hotelDAO.findById(hotelId);
            if (hotel == null) {
                response.sendRedirect(request.getContextPath() + "/search");
                return;
            }

            request.setAttribute("hotel", hotel);
            request.setAttribute("roomName", request.getParameter("room"));
            request.setAttribute("roomPrice", request.getParameter("price"));
            request.setAttribute("checkIn", request.getParameter("checkIn"));
            request.setAttribute("checkOut", request.getParameter("checkOut"));

            request.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/search");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String hotelIdStr = request.getParameter("hotelId");
            Long hotelId = (hotelIdStr != null && !hotelIdStr.trim().isEmpty()) ? Long.parseLong(hotelIdStr.trim()) : null;
            Hotel hotel = hotelId != null ? hotelDAO.findById(hotelId) : null;

            String guestName = request.getParameter("guestName");
            String guestEmail = request.getParameter("guestEmail");
            String guestPhone = request.getParameter("guestPhone");
            String roomType = request.getParameter("roomType");
            String checkInStr = request.getParameter("checkIn");
            String checkOutStr = request.getParameter("checkOut");
            String nightsStr = request.getParameter("nights");
            String totalPriceStr = request.getParameter("totalPrice");
            String paymentMethod = request.getParameter("paymentMethod");
            String[] requestsArray = request.getParameterValues("specialRequests");
            String specialNotes = request.getParameter("specialNotes");

            LocalDate checkIn = LocalDate.now().plusDays(1);
            LocalDate checkOut = LocalDate.now().plusDays(2);
            if (checkInStr != null && !checkInStr.trim().isEmpty()) {
                try {
                    checkIn = LocalDate.parse(checkInStr.trim(), DateTimeFormatter.ISO_LOCAL_DATE);
                } catch (Exception ignored) {}
            }
            if (checkOutStr != null && !checkOutStr.trim().isEmpty()) {
                try {
                    checkOut = LocalDate.parse(checkOutStr.trim(), DateTimeFormatter.ISO_LOCAL_DATE);
                } catch (Exception ignored) {}
            }

            int nights = 1;
            if (nightsStr != null && !nightsStr.trim().isEmpty()) {
                try {
                    nights = Math.max(1, Integer.parseInt(nightsStr.trim()));
                } catch (Exception ignored) {}
            }

            long totalPrice = 0L;
            if (totalPriceStr != null && !totalPriceStr.trim().isEmpty()) {
                try {
                    totalPrice = Long.parseLong(totalPriceStr.replaceAll("[^0-9]", ""));
                } catch (Exception ignored) {}
            }
            if (totalPrice == 0 && hotel != null) {
                totalPrice = hotel.getPrice() * nights;
            }

            String specialRequests = requestsArray != null ? String.join(", ", requestsArray) : "";

            int pointsEarned = (int) (totalPrice / 10000); // 1 point per 10,000 VND

            String bookingCode = "VS" + System.currentTimeMillis();

            HttpSession session = request.getSession(false);
            User currentUser = null;
            if (session != null && session.getAttribute("user") != null) {
                currentUser = (User) session.getAttribute("user");
                currentUser.setVPoints((currentUser.getVPoints() != null ? currentUser.getVPoints() : 0) + pointsEarned);
                userDAO.update(currentUser);
                session.setAttribute("user", currentUser);
            }

            Booking booking = Booking.builder()
                    .bookingCode(bookingCode)
                    .hotel(hotel)
                    .hotelName(hotel != null ? hotel.getName() : "Vstay Partner Hotel")
                    .roomType(roomType != null ? roomType : "Phòng tiêu chuẩn")
                    .guestName(guestName != null ? guestName : "Khách hàng")
                    .guestEmail(guestEmail != null ? guestEmail : "")
                    .guestPhone(guestPhone != null ? guestPhone : "")
                    .checkIn(checkIn)
                    .checkOut(checkOut)
                    .nights(nights)
                    .totalPrice(totalPrice)
                    .vPointsEarned(pointsEarned)
                    .paymentMethod(paymentMethod != null ? paymentMethod : "Thanh toán tại quầy")
                    .specialRequests(specialRequests)
                    .specialNotes(specialNotes)
                    .paymentStatus("PAID")
                    .user(currentUser)
                    .build();

            bookingDAO.save(booking);

            response.sendRedirect(request.getContextPath() + "/checkout?success=true&code=" + bookingCode);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi xử lý đặt phòng: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(request, response);
        }
    }
}
