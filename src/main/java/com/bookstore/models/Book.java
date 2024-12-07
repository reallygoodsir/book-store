package com.bookstore.models;

import java.util.List;

public class Book {
    private String isbn;
    private String title;
    private String publisher;
    private double price;
    private String description;
    private String publishDate;
    private String coverImageFile;
    private int inventory;
    private List<Author> authors;
    private List<Genre> genres;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getCoverImageFile() {
        return coverImageFile;
    }

    public void setCoverImageFile(String coverImageFile) {
        this.coverImageFile = coverImageFile;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    @Override
    public String toString() {
        return "Book{" +
                "isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", publisher='" + publisher + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", publishDate='" + publishDate + '\'' +
                ", coverImageFile='" + coverImageFile + '\'' +
                ", inventory=" + inventory +
                ", authors=" + authors +
                ", genres=" + genres +
                '}';
    }
}

