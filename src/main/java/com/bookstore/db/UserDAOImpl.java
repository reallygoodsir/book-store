package com.bookstore.db;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.bookstore.exceptions.UserNotFoundException;
import com.bookstore.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserDAOImpl implements UserDAO {
    private static final Logger logger = LogManager.getLogger(UserDAOImpl.class);
    private static final String SELECT_USER_BY_USERNAME = "select username, f_name, l_name, email, phone, passwd, " +
            "signup_date, last_login, is_staff from users " +
            "where username = ?";
    private static final String SELECT_USERS = "select username, f_name, l_name, email, phone, passwd, " +
            "signup_date, last_login, is_staff from users";
    private static final String DELETE_USER = "delete from users where username = ?";
    private static final String UPDATE_USER = "update users set f_name = ?, l_name = ?, email = ?, " +
            "phone = ?, passwd = ?, signup_date = ?, last_login = ? " +
            "where username = ?";
    private static final String ADD_USER = "insert into users (username, f_name, l_name, email, phone, " +
            "passwd, signup_date, last_login, is_staff) " +
            "values(?, ?, ?, ?, ?, ?, now(), now() , ?)";

    public Optional<User> selectUser(String userName) throws UserNotFoundException {
        try (
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
                PreparedStatement stmtSelectUser = connection.prepareStatement(SELECT_USER_BY_USERNAME)
        ) {
            stmtSelectUser.setString(1, userName);
            ResultSet resultSet = stmtSelectUser.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setUserName(resultSet.getString(1));
                user.setFirstName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setEmail(resultSet.getString(4));
                user.setPhoneNumber(resultSet.getString(5));
                user.setPasswd(resultSet.getString(6));
                user.setSignDate(resultSet.getString(7));
                user.setLastDate(resultSet.getString(8));
                user.setStaff(resultSet.getBoolean(9));
                return Optional.of(user);
            } else {
                throw new UserNotFoundException("User was not found with name " + userName);
            }
        } catch (UserNotFoundException userNotFoundException) {
            throw userNotFoundException;
        } catch (Exception exception) {
            logger.error("Error while selecting the user", exception);
            return Optional.empty();
        }
    }

    public List<User> selectUsers() {
        try (
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
                PreparedStatement stmtSelectUsers = connection.prepareStatement(SELECT_USERS)
        ) {
            ResultSet resultSet = stmtSelectUsers.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = new User();
                user.setUserName(resultSet.getString(1));
                user.setFirstName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setEmail(resultSet.getString(4));
                user.setPhoneNumber(resultSet.getString(5));
                user.setPasswd(resultSet.getString(6));
                user.setSignDate(resultSet.getString(7));
                user.setLastDate(resultSet.getString(8));
                user.setStaff(resultSet.getBoolean(9));
                users.add(user);
            }
            return users;
        } catch (Exception exception) {
            logger.error("Error selecting users", exception);
            return Collections.emptyList();
        }
    }

    public boolean registerUser(User user) {
        try (
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
                PreparedStatement stmtInsertUser = connection.prepareStatement(ADD_USER)
        ) {
            stmtInsertUser.setString(1, user.getUserName());
            stmtInsertUser.setString(2, user.getFirstName());
            stmtInsertUser.setString(3, user.getLastName());
            stmtInsertUser.setString(4, user.getEmail());
            stmtInsertUser.setInt(5, Integer.parseInt(user.getPhoneNumber()));
            stmtInsertUser.setString(6, user.getPasswd());
            int userIsStaff;
            if ((user.isStaff())) {
                userIsStaff = 1;
            } else {
                userIsStaff = 0;
            }
            stmtInsertUser.setInt(7, userIsStaff);
            int addUserResult = stmtInsertUser.executeUpdate();
            if (addUserResult == 1) {
                return true;
            } else {
                throw new Exception("SQL query did not return expected value for user: " + user);
            }
        } catch (Exception exception) {
            logger.error("Error inserting user", exception);
            return false;
        }
    }

    public boolean updateUser(User user) {
        try (
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
                PreparedStatement stmtUpdateUser = connection.prepareStatement(UPDATE_USER)
        ) {
            stmtUpdateUser.setString(1, user.getFirstName());
            stmtUpdateUser.setString(2, user.getLastName());
            stmtUpdateUser.setString(3, user.getEmail());
            stmtUpdateUser.setInt(4, Integer.parseInt(user.getPhoneNumber()));
            stmtUpdateUser.setString(5, user.getPasswd());

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(user.getSignDate(), dateFormatter);
            Date userSignUpDate = Date.valueOf(localDate);
            stmtUpdateUser.setDate(6, userSignUpDate);

            DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime userSignUpDateTime = LocalDateTime.parse(user.getLastDate(), timestampFormatter);
            Timestamp userLastLoginTimestamp = Timestamp.valueOf(userSignUpDateTime);
            stmtUpdateUser.setTimestamp(7, userLastLoginTimestamp);

            stmtUpdateUser.setString(8, user.getUserName());
            int updateUserResult = stmtUpdateUser.executeUpdate();
            if (updateUserResult == 1) {
                return true;
            } else {
                throw new Exception("SQL query did not return expected value for user: " + user);
            }
        } catch (Exception exception) {
            logger.error("Error updating user", exception);
            return false;
        }
    }

    public boolean deleteUser(String userName) {
        try (
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
                PreparedStatement stmtDeleteUser = connection.prepareStatement(DELETE_USER)
        ) {
            stmtDeleteUser.setString(1, userName);
            int deleteUserResult = stmtDeleteUser.executeUpdate();
            if (deleteUserResult == 1) {
                return true;
            } else {
                throw new Exception("SQL query did not return expected value for user: " + userName);
            }
        } catch (Exception exception) {
            logger.error("Error deleting user", exception);
            return false;
        }
    }
}
