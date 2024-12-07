package com.bookstore.models;

public class Review {
    private int rating;
    private String userName;
    private String isbn;
    private String reviewText;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getISBN() {
        return isbn;
    }

    public void setISBN(String isbn) {
        this.isbn = isbn;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    @Override
    public String toString() {
        return "Review{" +
                "rating=" + rating +
                ", userName='" + userName + '\'' +
                ", isbn='" + isbn + '\'' +
                ", reviewText='" + reviewText + '\'' +
                '}';
    }
}

