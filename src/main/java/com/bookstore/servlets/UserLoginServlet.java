package com.bookstore.servlets;

import com.bookstore.models.Credentials;
import com.bookstore.validators.UserLoginValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UserLoginServlet extends HeaderServlet {
    private static final Logger logger = LogManager.getLogger(UserLoginServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            super.doPost(request, response);
            logger.info("Started login");
            String userName = request.getParameter("username");
            String password = request.getParameter("passwd");

            Credentials credentials = new Credentials(userName, password);
            UserLoginValidator userLoginValidator = new UserLoginValidator();
            boolean isCredentialsEmpty = userLoginValidator.isCredentialsEmpty(credentials);
            logger.info("Checked credentials are empty [{}]", isCredentialsEmpty);
            if (isCredentialsEmpty) {
                request.setAttribute("credentialsError", "true");
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/SignUp.jsp");
                dispatcher.forward(request, response);
                return;
            }
            boolean isCredentialsValid = userLoginValidator.isCredentialsValid(credentials);
            logger.info("Checked credentials are valid [{}] for user [{}]", isCredentialsValid, userName);
            if (!isCredentialsValid) {
                request.setAttribute("credentialsError", "true");
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/SignUp.jsp");
                dispatcher.forward(request, response);
                return;
            }
            HttpSession session = request.getSession(true);
            if (session == null) {
                throw new Exception("Session has not been created for user " + userName);
            }
            session.setAttribute("userName", userName);
            response.sendRedirect(request.getContextPath() + "/");
            logger.info("Finished login for user [{}]", userName);
        } catch (Exception exception) {
            logger.error("Error during user login.", exception);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
    }
}

