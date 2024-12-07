package com.bookstore.converters;

import com.bookstore.models.Author;
import com.bookstore.models.Book;
import com.bookstore.models.Genre;
import org.apache.commons.fileupload.FileItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookConverter {
    public Map<String, String> convert(List<FileItem> fileItems) {
        Map<String, String> result = new HashMap<>();
        for (FileItem fileItem : fileItems) {
            if (fileItem.isFormField()) {
                result.put(fileItem.getFieldName(), fileItem.getString());
            } else {
                result.put("coverImageFile", fileItem.getName());
            }
        }
        return result;
    }

    public Book convert(Map<String, String> map) {
        Book book = new Book();
        book.setIsbn(map.get("isbn"));
        book.setTitle(map.get("title"));
        book.setDescription(map.get("description"));
        book.setPublisher(map.get("publisher"));
        book.setPrice(Double.parseDouble(map.get("price")));
        book.setPublishDate(map.get("publishDate"));
        book.setInventory(Integer.parseInt(map.get("inventory")));
        if (map.containsKey("coverImageFile")) {
            book.setCoverImageFile(map.get("coverImageFile"));
        }

        List<Genre> genres = new ArrayList<>();
        Genre genre1 = new Genre();
        String genreName1 = map.get("genre1");
        if (genreName1 != null && !genreName1.isEmpty()) {
            genre1.setGenreName(genreName1);
            genres.add(genre1);
        }

        Genre genre2 = new Genre();
        String genreName2 = map.get("genre2");
        if (genreName2 != null && !genreName2.isEmpty()) {
            genre2.setGenreName(genreName2);
            genres.add(genre2);
        }

        List<Author> authors = new ArrayList<>();
        Author author1 = new Author();
        Author author2 = new Author();
        Author author3 = new Author();

        String authorName1 = map.get("author1");
        String authorName2 = map.get("author2");
        String authorName3 = map.get("author3");

        if (authorName1 != null && !authorName1.isEmpty()) {
            author1.setAuthorName(authorName1);
            authors.add(author1);
        }
        if (authorName2 != null && !authorName2.isEmpty()) {
            author2.setAuthorName(authorName2);
            authors.add(author2);
        }
        if (authorName3 != null && !authorName3.isEmpty()) {
            author3.setAuthorName(authorName3);
            authors.add(author3);
        }

        book.setAuthors(authors);
        book.setGenres(genres);
        return book;
    }
}


