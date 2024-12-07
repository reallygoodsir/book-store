package com.bookstore.models;

public class LineItem {
    private int lineItemId;
    private int transactionId;
    private String isbn;
    private int quantity;
    private String title;
    private double price;
    private double totalPrice;
    public int getLineItemId() {
        return lineItemId;
    }

    public void setLineItemId(int lineItemId) {
        this.lineItemId = lineItemId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "LineItem{" +
                "lineItemId=" + lineItemId +
                ", transactionId=" + transactionId +
                ", isbn='" + isbn + '\'' +
                ", quantity=" + quantity +
                ", title='" + title + '\'' +
                '}';
    }
}
