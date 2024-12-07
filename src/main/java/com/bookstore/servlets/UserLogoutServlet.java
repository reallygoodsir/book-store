package com.bookstore.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UserLogoutServlet extends HeaderServlet {
    private static final Logger logger = LogManager.getLogger(UserLogoutServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            super.doGet(request, response);
            logger.info("Started log out");
            HttpSession session = request.getSession(false);
            if (session == null) {
                throw new Exception("Session does not exist to perform logout");
            }
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/");
            logger.info("Finished log out");
        } catch (Exception exception) {
            logger.error("Error during user logout.", exception);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
    }
}
