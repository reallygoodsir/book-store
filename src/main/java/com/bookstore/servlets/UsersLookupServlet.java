package com.bookstore.servlets;

import com.bookstore.db.UserDAO;
import com.bookstore.db.UserDAOImpl;
import com.bookstore.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UsersLookupServlet extends HeaderServlet {
    private static final Logger logger = LogManager.getLogger(UsersLookupServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            super.doGet(request, response);
            UserDAO userDAO = new UserDAOImpl();
            List<User> users = userDAO.selectUsers();
            request.setAttribute("users", users);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/users.jsp");
            dispatcher.forward(request, response);
        } catch (Exception exception) {
            logger.error("Error during users lookup ", exception);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
    }
}
