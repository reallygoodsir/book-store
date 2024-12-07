package com.bookstore.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReportsDAOImpl implements ReportsDAO {
    private static final Logger logger = LogManager.getLogger(ReportsDAOImpl.class);
    private static final String SELECT_WEEKLY_PRICE =
            "select concat(year(purchase_date), '/', week(purchase_date)) as weeks, " +
                    "sum(total_price) as total_price " +
                    "from transactions " +
                    "group by concat(year(purchase_date), '/', week(purchase_date)) " +
                    "order by weeks asc";
    private static final String SELECT_MONTHLY_PRICE =
            "select concat(year(purchase_date), '/', month(purchase_date)) as months, " +
                    "sum(total_price) as total_price " +
                    "from transactions " +
                    "group by concat(year(purchase_date), '/', month(purchase_date)) " +
                    "order by months asc";

    public List<List<String>> getWeeklySales() {
        try (
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
                PreparedStatement stmtSelectWeeklyPrice = connection.prepareStatement(SELECT_WEEKLY_PRICE)
        ) {
            ResultSet resultSet = stmtSelectWeeklyPrice.executeQuery();
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
            List<List<String>> rows = new ArrayList<>();
            double previousRow = 0;
            while (resultSet.next()) {
                List<String> row = new ArrayList<>();
                row.add(resultSet.getString(1));
                row.add(numberFormat.format(resultSet.getDouble(2)));
                row.add(numberFormat.format(resultSet.getDouble(2) - previousRow));
                previousRow = resultSet.getDouble(2);
                rows.add(row);
            }
            return rows;
        } catch (Exception exception) {
            logger.error("Error while getting weekly sales", exception);
            return Collections.emptyList();
        }
    }

    public List<List<String>> getMonthlySales() {
        try (
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
                PreparedStatement stmtSelectMonthlyPrice = connection.prepareStatement(SELECT_MONTHLY_PRICE)
        ) {
            ResultSet resultSet = stmtSelectMonthlyPrice.executeQuery();
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
            List<List<String>> rows = new ArrayList<>();
            double previousRow = 0;
            while (resultSet.next()) {
                List<String> row = new ArrayList<>();
                row.add(resultSet.getString(1));
                row.add(numberFormat.format(resultSet.getDouble(2)));
                row.add(numberFormat.format(resultSet.getDouble(2) - previousRow));
                previousRow = resultSet.getDouble(2);
                rows.add(row);
            }
            return rows;
        } catch (Exception exception) {
            logger.error("Error while getting monthly sales", exception);
            return Collections.emptyList();
        }
    }
}

