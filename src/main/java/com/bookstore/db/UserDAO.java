package com.bookstore.db;

import com.bookstore.exceptions.UserNotFoundException;
import com.bookstore.models.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO extends BaseDAO {
    Optional<User> selectUser(String userName) throws UserNotFoundException;

    List<User> selectUsers();

    boolean registerUser(User user);

    boolean updateUser(User user);

    boolean deleteUser(String userName);
}
