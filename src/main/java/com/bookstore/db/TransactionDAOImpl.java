package com.bookstore.db;

import com.bookstore.models.LineItem;
import com.bookstore.models.Transaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransactionDAOImpl implements TransactionDAO {
    private static final Logger logger = LogManager.getLogger(TransactionDAOImpl.class);
    private static final String SELECT_TRANSACTIONS = "select * from transactions where username = ? order by transaction_id";
    private static final String INSERT_TRANSACTION = "insert into transactions (username, purchase_date, " +
            "total_price, is_processed) " +
            "values(?, now(), ?, ?)";
    private static final String INSERT_LINE_ITEM = "insert into line_item (transaction_id, isbn, quantity) values(?, ?, ?)";

    private static final String UPDATE_BOOK_INVENTORY = "call update_inventory_sp(?, ?)";

    public int insertTransaction(Transaction transaction, List<LineItem> lineItems) {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
            connection.setAutoCommit(false);
            int transactionId;
            try (PreparedStatement stmtInsertTransaction = connection.prepareStatement(INSERT_TRANSACTION, Statement.RETURN_GENERATED_KEYS)) {
                stmtInsertTransaction.setString(1, transaction.getUserName());
                stmtInsertTransaction.setBigDecimal(2, BigDecimal.valueOf(transaction.getTotalPrice()));
                stmtInsertTransaction.setInt(3, transaction.getIsProcessed());

                int insertTransactionResult = stmtInsertTransaction.executeUpdate();
                if (insertTransactionResult != 1) {
                    throw new Exception("Cannot save transaction as result is not 1");
                }

                try (ResultSet resultSet = stmtInsertTransaction.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        transactionId = resultSet.getInt(1);
                    } else {
                        throw new Exception("Cannot get generated key for transaction");
                    }
                }
            }

            if (lineItems == null || lineItems.isEmpty()) {
                throw new Exception("Expected line items but they are empty");
            }


            try (PreparedStatement stmtInsertLineItem = connection.prepareStatement(INSERT_LINE_ITEM)) {
                for (LineItem item : lineItems) {
                    item.setTransactionId(transactionId);
                    stmtInsertLineItem.setInt(1, transactionId);
                    stmtInsertLineItem.setString(2, item.getIsbn());
                    stmtInsertLineItem.setInt(3, item.getQuantity());

                    int lineItemInsertResult = stmtInsertLineItem.executeUpdate();
                    if (lineItemInsertResult != 1) {
                        throw new SQLException("Couldn't insert line item " + item);
                    }
                }
            }

            try (CallableStatement stmtUpdateBookInventory = connection.prepareCall(UPDATE_BOOK_INVENTORY)) {
                for (LineItem item : lineItems) {
                    stmtUpdateBookInventory.setString(1, item.getIsbn());
                    stmtUpdateBookInventory.setInt(2, item.getQuantity());
                    int bookInventoryUpdateResult = stmtUpdateBookInventory.executeUpdate();
                    if (bookInventoryUpdateResult != 1) {
                        throw new SQLException("Couldn't update book inventory for item " + item);
                    }
                }
            }

            connection.commit();
            return transactionId;

        } catch (Exception exception) {
            logger.error("Exception during inserting a transaction", exception);
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (Exception rollbackException) {
                logger.error("Error while rolling back connection", rollbackException);

            }
            return -1;

        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception closeException) {
                logger.error("Error while closing connection", closeException);
            }
        }
    }


    public List<Transaction> selectAllTransactions(String userName) {
        try (
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
                PreparedStatement stmtSelectTransactions = connection.prepareStatement(SELECT_TRANSACTIONS)
        ) {
            stmtSelectTransactions.setString(1, userName);
            List<Transaction> transactions = new ArrayList<>();
            ResultSet resultSet = stmtSelectTransactions.executeQuery();
            while (resultSet.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(resultSet.getInt(1));
                transaction.setUserName(resultSet.getString(2));
                transaction.setPurchaseDate(resultSet.getDate(3).toString());
                transaction.setTotalPrice(resultSet.getDouble(4));
                transaction.setIsProcessed(resultSet.getInt(5));
                transactions.add(transaction);
            }
            return transactions;
        } catch (Exception exception) {
            logger.error("Error during selecting all transactions for user [{}]", userName, exception);
            return Collections.emptyList();
        }
    }
}
