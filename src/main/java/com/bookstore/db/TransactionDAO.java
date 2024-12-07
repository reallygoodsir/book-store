package com.bookstore.db;

import com.bookstore.models.LineItem;
import com.bookstore.models.Transaction;

import java.sql.SQLException;
import java.util.List;

public interface TransactionDAO extends BaseDAO {
    int insertTransaction(Transaction transaction, List<LineItem> lineItems) throws SQLException;

    List<Transaction> selectAllTransactions(String userName);
}
