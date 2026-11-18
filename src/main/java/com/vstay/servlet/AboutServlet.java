package com.poly.servlet;

import com.poly.dao.ContactDAO;
import com.poly.model.Contact;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "AboutServlet", urlPatterns = {"/about"})
public class AboutServlet extends HttpServlet {

    private final ContactDAO contactDAO = new ContactDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/about.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String topic = request.getParameter("topic");
        String message = request.getParameter("message");

        if (name != null && !name.trim().isEmpty() && email != null && !email.trim().isEmpty()) {
            Contact contact = Contact.builder()
                    .name(name.trim())
                    .phone(phone != null ? phone.trim() : "")
                    .email(email.trim())
                    .topic(topic != null ? topic.trim() : "")
                    .message(message != null ? message.trim() : "")
                    .build();

            contactDAO.save(contact);
            request.setAttribute("contactSuccess", true);
        } else {
            request.setAttribute("contactError", "Vui lòng nhập tên và địa chỉ email hợp lệ.");
        }

        request.getRequestDispatcher("/WEB-INF/views/about.jsp").forward(request, response);
    }
}
