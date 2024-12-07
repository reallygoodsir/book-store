package com.bookstore.servlets;

import com.bookstore.db.TransactionDAO;
import com.bookstore.db.TransactionDAOImpl;
import com.bookstore.models.Cart;
import com.bookstore.models.LineItem;
import com.bookstore.models.Transaction;
import com.bookstore.service.CartService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class SubmitOrderServlet extends HeaderServlet {
    private static final Logger logger = LogManager.getLogger(SubmitOrderServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            super.doPost(request, response);
            HttpSession session = request.getSession();
            String userName = (String) session.getAttribute("userName");

            if (userName == null || userName.isEmpty()) {
                response.sendRedirect("./SignUp.jsp?checkErr");
                return;
            }

            Cart cart = (Cart) session.getAttribute("cart");
            if (cart == null) {
                response.sendRedirect("/index.jsp");
                return;
            }

            List<LineItem> existingLineItems = cart.getItems();
            CartService cartService = new CartService();
            List<LineItem> updatedLineItems = cartService.updateLineItems(existingLineItems);
            boolean enoughInventory = (existingLineItems.size() == updatedLineItems.size());
            cart.setItems(updatedLineItems);
            if (!enoughInventory) {
                response.sendRedirect("./cartUpdate?quanErr");
                return;
            }

            double lineItemsTotalPrice = cartService.calculateTotalPrice(cart.getItems());
            Transaction transaction = new Transaction();
            transaction.setTotalPrice(lineItemsTotalPrice);
            transaction.setUserName(userName);
            transaction.setIsProcessed(1);
            TransactionDAO transactionDAO = new TransactionDAOImpl();
            int transactionId = transactionDAO.insertTransaction(transaction, cart.getItems());
            if (transactionId == -1) {
                throw new RuntimeException("Couldn't insert a transaction");
            }
            session.removeAttribute("cart");
            request.setAttribute("transactionId", transactionId);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/OrderConfirmation.jsp");
            dispatcher.forward(request, response);
        } catch (Exception exception) {
            logger.error("Error while submitting error", exception);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
    }
}

