package com.bookstore.converters;

import com.bookstore.models.Author;

import java.util.List;
import java.util.stream.Collectors;

public class AuthorConverter {

    public String convertToAuthorNames(List<Author> authors) {
        if (authors == null || authors.isEmpty()) {
            return "";
        }
        return authors.stream()
                .map(Author::getAuthorName)
                .collect(Collectors.joining(", "));
    }
}
