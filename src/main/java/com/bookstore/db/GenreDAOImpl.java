package com.bookstore.db;

import com.bookstore.models.Genre;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenreDAOImpl implements GenreDAO {
    private static final Logger logger = LogManager.getLogger(GenreDAOImpl.class);
    private static final String SELECT_ALL_GENRES = "SELECT genre_name from genres";
    private static final String SELECT_GENRES_BY_ISBN =
            "select genre_name " +
                    "from genre_book " +
                    "where isbn = ?";
    public List<String> loadAllGenreNames() {
        try (
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
                PreparedStatement stmtSelectAllGenres = connection.prepareStatement(SELECT_ALL_GENRES)
        ) {
            ResultSet resultSet = stmtSelectAllGenres.executeQuery();
            List<String> genres = new ArrayList<>();
            while (resultSet.next()) {
                genres.add(resultSet.getString("genre_name"));
            }
            return genres;
        } catch (Exception exception) {
            logger.error("Error during getting all genres", exception);
            return Collections.emptyList();
        }
    }

    @Override
    public List<Genre> loadGenresByIsbn(String isbn) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
             PreparedStatement stmtSelectGenres = connection.prepareStatement(SELECT_GENRES_BY_ISBN)) {
            stmtSelectGenres.setString(1, isbn);
            ResultSet resultSet = stmtSelectGenres.executeQuery();
            List<Genre> genres = new ArrayList<>();
            while (resultSet.next()) {
                Genre genre = new Genre();
                genre.setGenreName(resultSet.getString("genre_name"));
                genres.add(genre);
            }
            if (genres.isEmpty()) {
                throw new Exception("Couldn't get any genres for the isbn " + isbn);
            }
            return genres;
        } catch (Exception exception) {
            logger.error("Error while getting genres using the isbn {}", isbn, exception);
            return Collections.emptyList();
        }
    }
}
