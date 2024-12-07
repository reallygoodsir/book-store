package com.bookstore.servlets;

import com.bookstore.db.*;
import com.bookstore.models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CustomerAccountServlet extends HeaderServlet {
    private static final Logger logger = LogManager.getLogger(CustomerAccountServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            super.doGet(request, response);
            logger.info("Started customer account");
            HttpSession session = request.getSession();
            String userName = (String) session.getAttribute("userName");
            TransactionDAO transactionDAO = new TransactionDAOImpl();
            List<Transaction> allTransactions = transactionDAO.selectAllTransactions(userName);
            logger.info("All transactions size [{}] for user [{}]", allTransactions.size(), userName);
            OrderDAO orderDAO = new OrderDAOImpl();
            Map<Transaction, List<Order>> transactions = new LinkedHashMap<>();
            for (Transaction transaction : allTransactions) {
                List<Order> orders = orderDAO.selectOrders(userName, transaction.getTransactionId());
                transactions.put(transaction, orders);
            }
            request.setAttribute("transactions", transactions);

            UserDAO userDAO = new UserDAOImpl();
            Optional<User> optionalUser = userDAO.selectUser(userName);
            if (!optionalUser.isPresent()) {
                throw new Exception("UserDAO selectUser returned couldn't return a user");
            }
            User user = optionalUser.get();
            request.setAttribute("user", user);

            ReviewDAO reviewDAO = new ReviewDAOImpl();
            List<Review> allReviews = reviewDAO.selectReviewsForUser(userName);
            BookDAO bookDAO = new BookDAOImpl();

            Map<Review, Book> reviews = new LinkedHashMap<>();
            for (Review review : allReviews) {
                Optional<Book> selectBook = bookDAO.selectBook(review.getISBN());
                if (selectBook.isPresent()) {
                    Book book = selectBook.get();
                    reviews.put(review, book);
                } else {
                    logger.warn("Could not find book for review [{}]", review);
                }
            }
            request.setAttribute("reviews", reviews);

            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/CustomerAccount.jsp");
            dispatcher.forward(request, response);
            logger.info("Finished customer account");
        } catch (Exception exception) {
            logger.error("Error during customer account.", exception);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
    }
}
