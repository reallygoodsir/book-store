package com.bookstore.db;

import com.bookstore.models.Book;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BookDAO extends BaseDAO {
    Optional<Book> selectBook(String isbn);

    boolean isbnExists(String isbn);

    List<Book> searchByQuery(String query);

    List<Book> selectAllBooks();

    boolean update(Book book);

    boolean create(Book book) throws Exception;

    Map<String, Integer> selectInventories(List<String> isbnList);
}
