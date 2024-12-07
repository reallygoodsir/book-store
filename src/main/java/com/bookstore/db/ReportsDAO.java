package com.bookstore.db;

import java.util.List;

public interface ReportsDAO extends BaseDAO {
    List<List<String>> getWeeklySales();

    List<List<String>> getMonthlySales();
}
