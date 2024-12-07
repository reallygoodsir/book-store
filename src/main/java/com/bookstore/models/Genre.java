package com.bookstore.models;

public class Genre {
    private String genreName;

    public Genre() {
        this.genreName = "";
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "genreName='" + genreName + '\'' +
                '}';
    }
}

