package com.bookstore.validators;

import com.bookstore.db.BookDAO;
import com.bookstore.db.BookDAOImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BookValidator {
    private static final Logger logger = LogManager.getLogger(BookValidator.class);

    public boolean isValidISBN(String isbn) {
        if (isbn == null) {
            return false;
        }
        String isbnRegex = "^(?:\\d{9}[\\dX]|(?:\\d{1,5}-)?\\d{1,7}-\\d{1,6}-[\\dX])|(?:\\d{13}|(?:\\d{3}-)?\\d{1,5}-\\d{1,7}-\\d{1,6}-\\d)$";

        return isbn.matches(isbnRegex);
    }

    public boolean isDuplicateISBN(String isbn) throws Exception {
        try {
            if (isbn == null || isbn.isEmpty()) {
                throw new Exception("Given ISBN parameter is null");
            }
            BookDAO bookDAO = new BookDAOImpl();
            return bookDAO.isbnExists(isbn);
        } catch (Exception exception) {
            logger.error("Error while verifying isbn uniqueness [{}]", isbn, exception);
            throw new Exception("Error while verifying isbn uniqueness");
        }
    }

    public boolean isDuplicateGenres(String genre1, String genre2) {
        return genre1.equalsIgnoreCase(genre2);
    }

    public boolean isDuplicateAuthors(String author1, String author2, String author3) {
        if (author1.equalsIgnoreCase(author3)) {
            return true;
        }
        if (author1.equalsIgnoreCase(author2)) {
            return true;
        }
        if (!author2.equalsIgnoreCase("")) {
            return author2.equalsIgnoreCase(author3);
        }
        return false;
    }

    public boolean isValidPrice(String stringPrice) throws Exception {
        try {
            if (stringPrice == null) {
                throw new Exception("Price is null");
            }
            BigDecimal price = new BigDecimal(stringPrice);
            BigDecimal maximumValue = new BigDecimal("10000.00");
            if (price.compareTo(BigDecimal.ZERO) < 0) {
                return false;
            }
            return maximumValue.compareTo(price) >= 0;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }

    public boolean isValidPublishDate(String publishDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(publishDate, formatter);
            return !date.isAfter(LocalDate.now());
        } catch (Exception exception) {
            logger.error("Error while verifying publish date uniqueness", exception);
            return false;
        }
    }

    public boolean isValidInventory(String stringInventory) {
        try {
            if (stringInventory == null) {
                return false;
            }
            int inventory = Integer.parseInt(stringInventory);
            return inventory >= 0;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }
}
