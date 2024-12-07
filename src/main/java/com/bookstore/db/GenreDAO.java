package com.bookstore.db;

import com.bookstore.models.Genre;

import java.util.List;

public interface GenreDAO extends BaseDAO {
    List<String> loadAllGenreNames();
    List<Genre> loadGenresByIsbn(String isbn);
}
