package com.bookstore.service;

import com.bookstore.db.BookDAO;
import com.bookstore.db.BookDAOImpl;
import com.bookstore.models.Book;
import com.bookstore.models.Cart;
import com.bookstore.models.LineItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CartService {
    private static final Logger logger = LogManager.getLogger(CartService.class);

    public void addLineItemToCart(Cart cart, LineItem item) {
        String code = item.getIsbn();
        int quantity = item.getQuantity();
        for (int i = 0; i < cart.getItems().size(); i++) {
            LineItem lineItem = cart.getItems().get(i);
            if (lineItem.getIsbn().equals(code)) {
                lineItem.setQuantity(quantity);
                double totalPrice = calculateTotalPrice(lineItem);
                lineItem.setTotalPrice(totalPrice);
                return;
            }
        }
        cart.getItems().add(item);
    }

    public void removeLineItemFromCart(Cart cart, LineItem item) {
        String code = item.getIsbn();
        for (int i = 0; i < cart.getItems().size(); i++) {
            LineItem lineItem = cart.getItems().get(i);
            if (lineItem.getIsbn().equals(code)) {
                cart.getItems().remove(i);
                return;
            }
        }
    }

    public List<LineItem> updateLineItems(List<LineItem> items) {
        BookDAO bookDAO = new BookDAOImpl();
        List<String> isbnList = new ArrayList<>();
        for (LineItem item : items) {
            isbnList.add(item.getIsbn());
        }
        Map<String, Integer> isbnToInventoryMap = bookDAO.selectInventories(isbnList);

        List<LineItem> result = new ArrayList<>();
        for (LineItem item : items) {
            Integer inventory = isbnToInventoryMap.get(item.getIsbn());
            if (item.getQuantity() <= inventory) {
                result.add(item);
            }
        }


        return result;
    }

    public double calculateTotalPrice(List<LineItem> items) {
        double totalPrice = 0;
        for (LineItem item : items) {
            totalPrice += calculateTotalPrice(item);
        }
        return totalPrice;
    }

    private double calculateTotalPrice(LineItem lineItem) {
        try {
            Optional<Book> selectBook = new BookDAOImpl().selectBook(lineItem.getIsbn());
            if (!selectBook.isPresent()) {
                throw new RuntimeException("Couldn't get a book");
            }
            Book book = selectBook.get();
            return lineItem.getQuantity() * book.getPrice();
        } catch (Exception exception) {
            logger.error("Error getting total price double", exception);
        }
        return -1;
    }
}
