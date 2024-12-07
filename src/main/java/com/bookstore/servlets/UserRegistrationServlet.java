package com.bookstore.servlets;

import com.bookstore.validators.UserRegistrationValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserRegistrationServlet extends HeaderServlet {
    private static final Logger logger = LogManager.getLogger(UserRegistrationServlet.class);
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            super.doPost(request, response);
            UserRegistrationValidator userRegistrationService = new UserRegistrationValidator();

            String userName = request.getParameter("userName");
            if (!userRegistrationService.isUserNameValid(userName)) {
                request.setAttribute("userNameError", "true");
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/SignUp.jsp");
                dispatcher.forward(request, response);
                return;
            }
            if (userRegistrationService.isUserNameDuplicate(userName)) {
                request.setAttribute("userNameIsDuplicate", "true");
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/SignUp.jsp");
                dispatcher.forward(request, response);
                return;
            }

            String firstName = request.getParameter("firstName");
            if (!userRegistrationService.isFirstNameValid(firstName)) {
                request.setAttribute("firstNameError", "true");
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/SignUp.jsp");
                dispatcher.forward(request, response);
                logger.info("firstName requirements not met {}", firstName);
                return;
            }

            String lastName = request.getParameter("lastName");
            if (!userRegistrationService.isLastNameValid(lastName)) {
                request.setAttribute("lastNameError", "true");
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/SignUp.jsp");
                dispatcher.forward(request, response);
                return;
            }

            String password = request.getParameter("password");
            if (!userRegistrationService.isPasswordValid(password)) {
                request.setAttribute("passwordError", "true");
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/SignUp.jsp");
                dispatcher.forward(request, response);
                return;
            }

            String email = request.getParameter("email");
            if (!userRegistrationService.isEmailValid(email)) {
                request.setAttribute("emailError", "true");
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/SignUp.jsp");
                dispatcher.forward(request, response);
                return;
            }

            String phoneNumber = request.getParameter("phoneNumber");
            if (!userRegistrationService.isPhoneNumberValid(phoneNumber)) {
                request.setAttribute("phoneNumberError", "true");
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/SignUp.jsp");
                dispatcher.forward(request, response);
                return;
            }

            boolean isRegistered = userRegistrationService.register(userName, firstName, lastName,
                    password, email, phoneNumber);
            request.setAttribute("isRegistered", isRegistered);
            request.setAttribute("userName", userName);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/SignUpConfirmation.jsp");
            dispatcher.forward(request, response);
        } catch (Exception exception) {
            logger.error("Error during user login ", exception);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/SignUp.jsp");
            dispatcher.forward(request, response);
        }
    }
}
