package com.bookstore.db;

import com.bookstore.models.Author;

import java.util.List;

public interface AuthorsDAO extends BaseDAO {

    List<Author> loadAuthorsByIsbn(String isbn);
}
