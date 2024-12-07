package com.bookstore.models;

import java.math.BigDecimal;

public class Order {
    private String isbn;
    private BigDecimal totalPrice;

    private Integer quantity;

    private String title;

    private Double price;

    private String imageName;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Override
    public String toString() {
        return "Order{" +
                "isbn='" + isbn + '\'' +
                ", totalPrice=" + totalPrice +
                ", quantity=" + quantity +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", imageName='" + imageName + '\'' +
                '}';
    }
}
