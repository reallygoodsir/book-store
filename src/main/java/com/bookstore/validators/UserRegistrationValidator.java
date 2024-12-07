package com.bookstore.validators;

import com.bookstore.db.UserDAO;
import com.bookstore.db.UserDAOImpl;
import com.bookstore.exceptions.UserNotFoundException;
import com.bookstore.models.User;
import com.bookstore.util.Hasher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.regex.Pattern;

public class UserRegistrationValidator {
    private static final Logger logger = LogManager.getLogger(UserRegistrationValidator.class);

    public boolean isUserNameValid(String userName) {
        if (userName == null || userName.isEmpty()) {
            return false;
        }
        String regex = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{2,18}$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(userName).matches();
    }

    public boolean isUserNameDuplicate(String userName) throws Exception{
        try {
            UserDAO userDAO = new UserDAOImpl();
            Optional<User> user = userDAO.selectUser(userName);
            if(user.isPresent()){
                return true;
            }else{
                throw new UserNotFoundException("User hasn't been found");
            }
        } catch (UserNotFoundException userNotFoundException) {
            return false;
        }
        catch(Exception exception){
            logger.error("Error while checking whether user is duplicate", exception);
            throw new Exception(exception.getMessage());
        }
    }

    public boolean isFirstNameValid(String firstName) {
        if (firstName == null || firstName.isEmpty()) {
            return false;
        }

        String regex = "^[A-Za-z]{3,15}$";
        return Pattern.matches(regex, firstName);
    }

    public boolean isLastNameValid(String lastName) {
        if (lastName == null || lastName.isEmpty()) {
            return false;
        }

        String regex = "^[A-Za-z]{3,15}$";
        return Pattern.matches(regex, lastName);
    }

    public boolean isPasswordValid(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }

        String regex = "^.{8,40}$";
        return Pattern.matches(regex, password);
    }

    public boolean isEmailValid(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return Pattern.matches(regex, email);
    }

    public boolean isPhoneNumberValid(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }

        String regex = "^\\+?[1-9]\\d{1,14}$";
        return Pattern.matches(regex, phoneNumber);
    }

    public boolean register(String userName, String firstName, String lastName,
                            String password, String email, String phoneNumber) {
        User user = new User();
        user.setUserName(userName);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        String hashedPassword = Hasher.getHash(password);
        user.setPasswd(hashedPassword);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        UserDAO userDAO = new UserDAOImpl();
        return userDAO.registerUser(user);

    }
}
