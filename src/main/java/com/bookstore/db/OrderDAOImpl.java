package com.bookstore.db;

import com.bookstore.models.Order;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {
    private static final Logger logger = LogManager.getLogger(OrderDAOImpl.class);
    private static final String SELECT_ORDER = "SELECT " +
            "    t.total_price, " +
            "    l.isbn, " +
            "    l.quantity, " +
            "    b.title, " +
            "    b.price, " +
            "    b.cover_image " +
            "FROM " +
            "    transactions t " +
            "JOIN " +
            "    line_item l ON t.transaction_id = l.transaction_id " +
            "JOIN " +
            "    books b ON b.isbn = l.isbn " +
            "WHERE " +
            "    t.username = ? " +
            "    AND t.is_processed = '1' " +
            "    AND t.transaction_id = ? " +
            "ORDER BY " +
            "    t.transaction_id; ";


    public List<Order> selectOrders(String userName, int transactionId) {
        try (
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
                PreparedStatement stmtSelectOrder = connection.prepareStatement(SELECT_ORDER)
        ) {
            stmtSelectOrder.setString(1, userName);
            stmtSelectOrder.setInt(2, transactionId);
            ResultSet resultSet = stmtSelectOrder.executeQuery();
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                Order order = new Order();
                order.setIsbn(resultSet.getString("isbn"));
                order.setPrice((double) resultSet.getInt("price"));
                order.setQuantity(resultSet.getInt("quantity"));
                order.setImageName(resultSet.getString("cover_image"));
                order.setTitle(resultSet.getString("title"));
                order.setTotalPrice(resultSet.getBigDecimal("total_price"));
                orders.add(order);
            }
            return orders;
        } catch (Exception exception) {
            logger.error("Error selecting orders for user [{}] and transaction id [{}]",
                    userName, transactionId, exception);
            return Collections.emptyList();
        }
    }
}
