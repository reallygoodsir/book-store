package com.bookstore.converters;

import com.bookstore.models.Genre;

import java.util.List;
import java.util.stream.Collectors;

public class GenreConverter {
    public String convertToGenreNames(List<Genre> genres) {
        if (genres == null || genres.isEmpty()) {
            return "";
        }
        return genres.stream()
                .map(Genre::getGenreName)
                .collect(Collectors.joining(", "));
    }
}
