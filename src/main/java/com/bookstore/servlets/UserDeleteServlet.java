package com.bookstore.servlets;

import com.bookstore.db.UserDAO;
import com.bookstore.db.UserDAOImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserDeleteServlet extends HeaderServlet {
    private static final Logger logger = LogManager.getLogger(UserDeleteServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            super.doPost(request, response);
            String userName = request.getParameter("userName");
            UserDAO userDAO = new UserDAOImpl();
            boolean isUserDeleted = userDAO.deleteUser(userName);
            if (isUserDeleted) {
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
                dispatcher.forward(request, response);
            } else {
                throw new Exception("User was not deleted successfully");
            }
        } catch (Exception exception) {
            logger.error("Error while deleting user", exception);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
    }
}

