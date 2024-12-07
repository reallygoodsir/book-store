package com.bookstore.models;

public class Transaction {
    private int transactionId;
    private String username;
    private String purchaseDate;
    private double totalPrice;
    private int isProcessed;

    public void setTransactionId(int transaction_id) {
        this.transactionId = transaction_id;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public void setPurchaseDate(String purchase_date) {
        this.purchaseDate = purchase_date;
    }

    public void setTotalPrice(double total_price) {
        this.totalPrice = total_price;
    }

    public void setIsProcessed(int isProcessed) {
        this.isProcessed = isProcessed;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public String getUserName() {
        return username;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getIsProcessed() {
        return isProcessed;
    }
}
