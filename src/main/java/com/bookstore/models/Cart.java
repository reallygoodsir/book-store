package com.bookstore.models;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<LineItem> items;

    public Cart(ArrayList<LineItem> items) {
        this.items = items;
    }

    public Cart() {
        this.items = new ArrayList<LineItem>();
    }

    public List<LineItem> getItems() {
        return items;
    }

    public void setItems(List<LineItem> items) {
        this.items = items;
    }

}

