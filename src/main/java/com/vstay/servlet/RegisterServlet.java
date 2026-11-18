package com.vstay.servlet;

import com.vstay.dao.UserDAO;
import com.vstay.model.User;
import com.vstay.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/login?tab=register");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (fullName == null || fullName.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            request.setAttribute("registerError", "Vui lòng điền đầy đủ các thông tin bắt buộc.");
            request.setAttribute("tab", "register");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        if (confirmPassword != null && !password.equals(confirmPassword)) {
            request.setAttribute("registerError", "Mật khẩu xác nhận không khớp.");
            request.setAttribute("fullName", fullName);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.setAttribute("tab", "register");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        try {
            User existing = userDAO.findByEmail(email.trim());
            if (existing != null) {
                request.setAttribute("registerError", "Email này đã được đăng ký tài khoản.");
                request.setAttribute("fullName", fullName);
                request.setAttribute("phone", phone);
                request.setAttribute("tab", "register");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                return;
            }

            String salt = PasswordUtil.generateSalt();
            String passwordHash = PasswordUtil.hashPassword(password, salt);

            User user = User.builder()
                    .fullName(fullName.trim())
                    .email(email.trim().toLowerCase())
                    .phone(phone != null ? phone.trim() : "")
                    .passwordHash(passwordHash)
                    .salt(salt)
                    .vipTier("Silver")
                    .vPoints(100) // Welcome bonus points!
                    .role("USER")
                    .build();

            userDAO.save(user);

            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);

            response.sendRedirect(request.getContextPath() + "/");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("registerError", "Lỗi khi đăng ký tài khoản: " + e.getMessage());
            request.setAttribute("tab", "register");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}
