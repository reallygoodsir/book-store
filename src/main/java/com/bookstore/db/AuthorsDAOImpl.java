package com.bookstore.db;

import com.bookstore.models.Author;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AuthorsDAOImpl implements AuthorsDAO {
    private static final Logger logger = LogManager.getLogger(AuthorsDAOImpl.class);
    private static final String SELECT_AUTHORS_BY_ISBN =
            "select author.author_name, " +
                    "author.author_id " +
                    "from author " +
                    "join book_author " +
                    "on author.author_id = book_author.author_id " +
                    "where book_author.isbn = ?";

    @Override
    public List<Author> loadAuthorsByIsbn(String isbn) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
             PreparedStatement stmtSelectAuthors = connection.prepareStatement(SELECT_AUTHORS_BY_ISBN)) {
            stmtSelectAuthors.setString(1, isbn);
            ResultSet resultSet = stmtSelectAuthors.executeQuery();
            List<Author> authors = new ArrayList<>();
            while (resultSet.next()) {
                Author author = new Author();
                author.setAuthorName(resultSet.getString("author_name"));
                author.setAuthorId(resultSet.getInt("author_id"));
                authors.add(author);
            }
            if (authors.isEmpty()) {
                throw new Exception("Couldn't get any authors for the isbn " + isbn);
            }
            return authors;
        } catch (Exception exception) {
            logger.error("Error while getting authors using the isbn {}", isbn, exception);
            return Collections.emptyList();
        }
    }
}
