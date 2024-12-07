package com.bookstore.db;

import com.bookstore.models.Author;
import com.bookstore.models.Book;
import com.bookstore.models.Genre;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BookDAOImpl implements BookDAO {
    private static final Logger logger = LogManager.getLogger(BookDAOImpl.class);
    private static final String INSERT_BOOK = "insert into books " +
            "(isbn, title, publisher, price, description, publish_date, cover_image, inventory) " +
            "values(?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_AUTHOR = "insert into author " +
            " (author_name) values(?)";
    private static final String SELECT_AUTHOR = "select author_id, author_name from author  where author_name = ?";
    private static final String INSERT_BOOK_AUTHOR = "insert into book_author (isbn, author_id) values(?, ?)";
    private static final String INSERT_GENRE = "insert into genres (genre_name) values(?)";
    private static final String SELECT_GENRE = "select genre_name from genres where genre_name = ?";
    private static final String INSERT_BOOK_GENRE = "insert into genre_book (isbn, genre_name) values(?, ?)";
    private static final String SELECT_BOOK = "select isbn, title, publisher, price, " +
            "description, publish_date, cover_image, inventory " +
            "from books " +
            "where isbn = ?";
    private static final String SEARCH_FOR_BOOKS = "select b.isbn, b.title, b.publisher, b.price, " +
            "b.description, b.publish_date, b.cover_image, b.inventory " +
            "from books b " +
            "join book_author ba " +
            "on b.isbn=ba.isbn " +
            "join author a " +
            "on a.author_id=ba.author_id " +
            "join genre_book gb " +
            "on b.isbn=gb.isbn " +
            "join genres g " +
            "on gb.genre_name=g.genre_name " +
            "where b.isbn like ? " +
            "or b.title like ? " +
            "or a.author_name like ? " +
            "or g.genre_name like ? " +
            "or b.description like ?";

    private static final String SELECT_BOOKS = "select isbn, title, publisher, price, description, " +
            "publish_date, cover_image, inventory " +
            "from books";

    private static final String SELECT_BOOK_BY_ISBN = "select isbn " +
            "from books " +
            "where isbn = ?";

    private static final String UPDATE_BOOK = "update books set isbn = ?, title= ?, publisher= ?, price = ?, " +
            "description = ?, publish_date= ?, cover_image = ?, inventory = ? " +
            "where isbn = ?";

    private static final String SELECT_BOOKS_INVENTORY = "select isbn, inventory " +
            "from books " +
            "where isbn IN (";

    public Optional<Book> selectBook(String isbn) {
        try (
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
                PreparedStatement stmtSelectBook = connection.prepareStatement(SELECT_BOOK)
        ) {
            stmtSelectBook.setString(1, isbn);
            ResultSet resultSet = stmtSelectBook.executeQuery();
            if (resultSet.next()) {
                Book book = new Book();
                book.setIsbn(resultSet.getString(1));
                book.setTitle(resultSet.getString(2));
                book.setPublisher(resultSet.getString(3));
                book.setPrice(resultSet.getDouble(4));
                book.setDescription(resultSet.getString(5));
                book.setPublishDate(resultSet.getDate(6).toString());
                book.setCoverImageFile(resultSet.getString(7));
                book.setInventory(resultSet.getInt(8));
                return Optional.of(book);
            }
        } catch (Exception exception) {
            logger.error("Error during getting a book", exception);
        }
        return Optional.empty();
    }

    public boolean isbnExists(String isbn) {
        try (
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
                PreparedStatement stmtSelectBookISBN = connection.prepareStatement(SELECT_BOOK_BY_ISBN)
        ) {
            stmtSelectBookISBN.setString(1, isbn);
            ResultSet resultSet = stmtSelectBookISBN.executeQuery();
            boolean resultSetNext = resultSet.next();
            return resultSetNext;
        } catch (Exception exception) {
            logger.error("Error during getting a book", exception);
        }
        return false;
    }

    public List<Book> searchByQuery(String query) {
        try (
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
                PreparedStatement stmtSearchForBooks = connection.prepareStatement(SEARCH_FOR_BOOKS)
        ) {
            String searchQuery = "%" + query.replaceAll("'", "''") + "%";
            stmtSearchForBooks.setString(1, searchQuery);
            stmtSearchForBooks.setString(2, searchQuery);
            stmtSearchForBooks.setString(3, searchQuery);
            stmtSearchForBooks.setString(4, searchQuery);
            stmtSearchForBooks.setString(5, searchQuery);

            List<Book> books = new ArrayList<>();
            ResultSet resultSet = stmtSearchForBooks.executeQuery();
            while (resultSet.next()) {
                Book book = new Book();
                book.setIsbn(resultSet.getString(1));
                book.setTitle(resultSet.getString(2));
                book.setPublisher(resultSet.getString(3));
                book.setPrice(resultSet.getDouble(4));
                book.setDescription(resultSet.getString(5));
                book.setPublishDate(resultSet.getDate(6).toString());
                book.setCoverImageFile(resultSet.getString(7));
                book.setInventory(resultSet.getInt(8));
                if (isUniqueInList(books, book.getIsbn())) {
                    books.add(book);
                }
            }
            return books;
        } catch (Exception exception) {
            logger.error("Error while searching for books ", exception);
        }
        return Collections.emptyList();
    }

    public List<Book> selectAllBooks() {
        try (
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
                PreparedStatement stmtSelectAllBooks = connection.prepareStatement(SELECT_BOOKS)
        ) {
            List<Book> books = new ArrayList<>();
            ResultSet resultSet = stmtSelectAllBooks.executeQuery();
            while (resultSet.next()) {
                Book book = new Book();
                book.setIsbn(resultSet.getString(1));
                book.setTitle(resultSet.getString(2));
                book.setPublisher(resultSet.getString(3));
                book.setPrice(resultSet.getDouble(4));
                book.setDescription(resultSet.getString(5));
                book.setPublishDate(resultSet.getDate(6).toString());
                book.setCoverImageFile(resultSet.getString(7));
                book.setInventory(resultSet.getInt(8));
                books.add(book);
            }
            return books;
        } catch (Exception exception) {
            logger.error("Error while selecting all books ", exception);
        }
        return Collections.emptyList();
    }

    public boolean update(Book book) {
        try (
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
                PreparedStatement stmtUpdateBook = connection.prepareStatement(UPDATE_BOOK)
        ) {
            stmtUpdateBook.setString(1, book.getIsbn());
            stmtUpdateBook.setString(2, book.getTitle());
            stmtUpdateBook.setString(3, book.getPublisher());
            BigDecimal bookPrice = BigDecimal.valueOf(book.getPrice());
            stmtUpdateBook.setBigDecimal(4, bookPrice);
            stmtUpdateBook.setString(5, book.getDescription());

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(book.getPublishDate(), dateFormatter);
            Date bookPublishDate = Date.valueOf(localDate);
            stmtUpdateBook.setDate(6, bookPublishDate);

            stmtUpdateBook.setString(7, book.getCoverImageFile());
            stmtUpdateBook.setInt(8, book.getInventory());
            stmtUpdateBook.setString(9, book.getIsbn());
            int updateResult = stmtUpdateBook.executeUpdate();
            if (updateResult == 1) {
                return true;
            } else {
                throw new Exception("SQL query did not return expected value for book: " + book);
            }
        } catch (Exception exception) {
            logger.error("Error updating the book ", exception);
            return false;
        }
    }

    public boolean create(Book book) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
            connection.setAutoCommit(false);

            // Insert the book
            try (PreparedStatement stmtInsertBook = connection.prepareStatement(INSERT_BOOK)) {
                stmtInsertBook.setString(1, book.getIsbn());
                stmtInsertBook.setString(2, book.getTitle());
                stmtInsertBook.setString(3, book.getPublisher());
                stmtInsertBook.setBigDecimal(4, BigDecimal.valueOf(book.getPrice()));
                stmtInsertBook.setString(5, book.getDescription());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.parse(book.getPublishDate(), formatter);
                stmtInsertBook.setDate(6, Date.valueOf(localDate));
                stmtInsertBook.setString(7, book.getCoverImageFile());
                stmtInsertBook.setInt(8, book.getInventory());
                if (stmtInsertBook.executeUpdate() != 1) {
                    logger.error("Failed to insert book: {} ", book);
                    connection.rollback();
                    return false;
                }
            }

            // Handle authors
            for (Author author : book.getAuthors()) {
                int authorId = 0;
                String authorName = author.getAuthorName();

                try (PreparedStatement stmtSelectAuthor = connection.prepareStatement(SELECT_AUTHOR)) {
                    stmtSelectAuthor.setString(1, authorName);
                    try (ResultSet resultSet = stmtSelectAuthor.executeQuery()) {
                        if (resultSet.next()) {
                            authorId = resultSet.getInt(1);
                        }
                    }
                } catch (Exception exception) {
                    logger.error("Error selecting author {} ", authorName, exception);
                    connection.rollback();
                    return false;
                }

                if (authorId == 0) {
                    try (PreparedStatement stmtInsertAuthor = connection.prepareStatement(INSERT_AUTHOR, Statement.RETURN_GENERATED_KEYS)) {
                        stmtInsertAuthor.setString(1, authorName);
                        if (stmtInsertAuthor.executeUpdate() != 1) {
                            logger.error("Failed to insert author {} ", authorName);
                            connection.rollback();
                            return false;
                        }
                        try (ResultSet generatedKeys = stmtInsertAuthor.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                authorId = generatedKeys.getInt(1);
                            } else {
                                logger.error("Failed to retrieve generated keys for author: {} ", authorName);
                                connection.rollback();
                                return false;
                            }
                        }
                    }
                }

                author.setAuthorId(authorId);

                try (PreparedStatement stmtInsertBookAuthor = connection.prepareStatement(INSERT_BOOK_AUTHOR)) {
                    stmtInsertBookAuthor.setString(1, book.getIsbn());
                    stmtInsertBookAuthor.setInt(2, authorId);

                    logger.info(">>>>>>>>>> ISBN [{}], authorId [{}]", book.getIsbn(), authorId);
                    if (stmtInsertBookAuthor.executeUpdate() != 1) {
                        logger.error("Failed to connect book and author: {} | {}", book, author);
                        connection.rollback();
                        return false;
                    }
                }
            }

            // Handle genres
            for (Genre genre : book.getGenres()) {
                String genreName = genre.getGenreName();
                boolean genreExists;

                try (PreparedStatement stmtSelectGenre = connection.prepareStatement(SELECT_GENRE)) {
                    stmtSelectGenre.setString(1, genreName);
                    try (ResultSet resultSet = stmtSelectGenre.executeQuery()) {
                        genreExists = resultSet.next();
                    }
                } catch (Exception exception) {
                    logger.error("Error selecting genre: {} ", genreName, exception);
                    connection.rollback();
                    return false;
                }

                if (!genreExists) {
                    try (PreparedStatement stmtInsertGenre = connection.prepareStatement(INSERT_GENRE)) {
                        stmtInsertGenre.setString(1, genreName);
                        if (stmtInsertGenre.executeUpdate() != 1) {
                            logger.error("Failed to insert genre: {} ", genreName);
                            connection.rollback();
                            return false;
                        }
                    }
                }

                try (PreparedStatement stmtInsertBookGenre = connection.prepareStatement(INSERT_BOOK_GENRE)) {
                    stmtInsertBookGenre.setString(1, book.getIsbn());
                    stmtInsertBookGenre.setString(2, genreName);
                    if (stmtInsertBookGenre.executeUpdate() != 1) {
                        logger.error("Failed to connect book and genre: {} | {} ", genreName, genre);
                        connection.rollback();
                        return false;
                    }
                }
            }

            connection.commit();
            return true;
        } catch (Exception exception) {
            logger.error("Error creating the book", exception);
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (Exception rollbackException) {
                    logger.error("Error during rollback", rollbackException);
                }
            }
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception closeException) {
                    logger.error("Error closing connection", closeException);
                }
            }
        }
    }

    @Override
    public Map<String, Integer> selectInventories(List<String> isbnList) {
        StringBuilder selectBooksInventory = new StringBuilder(SELECT_BOOKS_INVENTORY);
        for (int i = 0; i < isbnList.size(); i++) {
            selectBooksInventory.append("?");
            if (i < isbnList.size() - 1) {
                selectBooksInventory.append(", ");
            }
        }
        selectBooksInventory.append(")");
        Map<String, Integer> inventories = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
             PreparedStatement stmtSelectBooksInventory = connection.prepareStatement(String.valueOf(selectBooksInventory))
        ) {
            for (int i = 0; i < isbnList.size(); i++) {
                int parameterIndex = i + 1;
                stmtSelectBooksInventory.setString(parameterIndex, isbnList.get(i));
            }
            try (ResultSet resultSet = stmtSelectBooksInventory.executeQuery()) {
                while (resultSet.next()) {
                    String isbn = resultSet.getString("isbn");
                    Integer inventory = resultSet.getInt("inventory");
                    logger.info("Inventory: {}", inventory);
                    inventories.put(isbn, inventory);
                }
            }
            return inventories;
        } catch (Exception exception) {
            logger.error("Error while selecting books inventory", exception);
            return Collections.emptyMap();
        }
    }

    private boolean isUniqueInList(List<Book> books, String isbn) {
        boolean isUnique = true;
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                isUnique = false;
                break;
            }
        }
        return isUnique;
    }
}

