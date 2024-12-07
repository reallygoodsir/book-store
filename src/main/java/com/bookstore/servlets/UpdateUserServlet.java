package com.bookstore.servlets;

import com.bookstore.db.UserDAO;
import com.bookstore.db.UserDAOImpl;
import com.bookstore.exceptions.UserNotFoundException;
import com.bookstore.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

public class UpdateUserServlet extends HeaderServlet {
    private static final Logger logger = LogManager.getLogger(UpdateUserServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            super.doPost(request, response);
            HttpSession session = request.getSession(false);
            String userName = (String) session.getAttribute("userName");
            String firstName = request.getParameter("fName");
            String lastName = request.getParameter("lName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            UserDAO userDAO = new UserDAOImpl();
            Optional<User> selectedUser = userDAO.selectUser(userName);
            if (!selectedUser.isPresent()) {
                throw new Exception("Error selecting user from DB with name " + userName);
            }
            User user = selectedUser.get();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPhoneNumber(phone);

            boolean isUserUpdated = userDAO.updateUser(user);
            if (isUserUpdated) {
                response.sendRedirect("./CustomerAccount");
                return;
            } else {
                throw new Exception("User was not updated successfully with name " + userName);
            }
        } catch (UserNotFoundException userNotFoundException) {
            logger.error("User not found for update.", userNotFoundException);
        } catch (Exception exception) {
            logger.error("Error updating user.", exception);
        }
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
        dispatcher.forward(request, response);
    }
}

