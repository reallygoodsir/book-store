package com.bookstore.servlets;

import com.bookstore.models.Cart;
import com.bookstore.models.LineItem;
import com.bookstore.service.CartService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class CartUpdateServlet extends HeaderServlet {
    private static final Logger logger = LogManager.getLogger(CartUpdateServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            super.doGet(request, response);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/Checkout.jsp");
            dispatcher.forward(request, response);
        }catch (Exception exception){
            logger.error("Error while redirecting to checkout", exception);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            super.doPost(request, response);
            String quantityParameter = request.getParameter("quantity");
            String isbn = request.getParameter("isbn");
            String title = request.getParameter("title");
            String removeButtonVal = request.getParameter("remove");
            String addButtonVal = request.getParameter("add");
            String quantityError = request.getParameter("quanErr");

            HttpSession session = request.getSession();

            Cart cart = (Cart) session.getAttribute("cart");
            if (cart == null) {
                cart = new Cart();
            }

            if (quantityError == null) {
                int quantity;
                if (addButtonVal != null) {
                    quantity = 1;
                } else if (removeButtonVal != null) {
                    quantity = 0;
                } else {
                    quantity = Integer.parseInt(quantityParameter);
                }

                LineItem lineItem = new LineItem();
                lineItem.setIsbn(isbn);
                lineItem.setQuantity(quantity);
                if (title != null) {
                    lineItem.setTitle(title);
                }
                CartService cartService = new CartService();
                cartService.addLineItemToCart(cart, lineItem);
                if (quantity > 0) {
                    cartService.addLineItemToCart(cart, lineItem);
                } else {
                    cartService.removeLineItemFromCart(cart, lineItem);
                }
            }
            session.setAttribute("cart", cart);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/Checkout.jsp");
            dispatcher.forward(request, response);
        } catch (Exception exception) {
            logger.error("Error while updating the cart", exception);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
    }

}

