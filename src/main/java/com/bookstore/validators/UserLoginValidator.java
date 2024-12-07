package com.bookstore.validators;

import com.bookstore.db.UserDAO;
import com.bookstore.db.UserDAOImpl;
import com.bookstore.exceptions.UserNotFoundException;
import com.bookstore.models.Credentials;
import com.bookstore.models.User;
import com.bookstore.service.CartService;
import com.bookstore.util.Hasher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class UserLoginValidator {
    private static final Logger logger = LogManager.getLogger(CartService.class);
    public boolean isCredentialsEmpty(Credentials credentials) {
        return credentials.getUserName() == null || credentials.getUserName().isEmpty()
                || credentials.getPassword() == null || credentials.getPassword().isEmpty();
    }

    public boolean isCredentialsValid(Credentials credentials) {
        try {
            UserDAO userDAO = new UserDAOImpl();
            Optional<User> selectedUser = userDAO.selectUser(credentials.getUserName());
            if (selectedUser.isPresent()) {
                User user = selectedUser.get();
                String hashedPassword = Hasher.getHash(credentials.getPassword());
                if (hashedPassword.equals(user.getPasswd())) {
                    return true;
                }
            }
        } catch (UserNotFoundException userNotFoundException) {
            logger.warn("User not found", userNotFoundException);
        }
        return false;
    }
}
