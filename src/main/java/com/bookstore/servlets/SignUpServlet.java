package com.bookstore.servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SignUpServlet extends HeaderServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        super.doGet(request, response);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/SignUp.jsp");
        dispatcher.forward(request, response);
    }
}
