package com.bookstore.db;

import com.bookstore.models.Order;

import java.util.List;

public interface OrderDAO extends BaseDAO {
    List<Order> selectOrders(String userName, int transactionId);
}
