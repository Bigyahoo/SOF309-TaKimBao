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

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String redirect = request.getParameter("redirect");

        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ email và mật khẩu.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        try {
            User user = userDAO.findByEmail(email.trim());
            if (user != null && PasswordUtil.verifyPassword(password, user.getSalt(), user.getPasswordHash())) {
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                userDAO.updateLastLogin(user.getId());

                if (redirect != null && !redirect.trim().isEmpty() && !redirect.contains("://")) {
                    response.sendRedirect(redirect);
                } else if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                    response.sendRedirect(request.getContextPath() + "/admin");
                } else {
                    response.sendRedirect(request.getContextPath() + "/");
                }
            } else {
                request.setAttribute("error", "Email hoặc mật khẩu không chính xác.");
                request.setAttribute("email", email);
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi khi đăng nhập: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}
