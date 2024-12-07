package com.bookstore.servlets;

import com.bookstore.db.ReviewDAO;
import com.bookstore.db.ReviewDAOImpl;
import com.bookstore.models.Review;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SubmitReviewServlet extends HeaderServlet {
    private static final Logger logger = LogManager.getLogger(SubmitReviewServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            super.doPost(request, response);
            HttpSession session = request.getSession();
            if (session == null) {
                throw new Exception("Session was not found.");
            }
            String userName = (String) session.getAttribute("userName");
            int rating = Integer.parseInt(request.getParameter("rating"));
            String reviewText = request.getParameter("review-text");
            String isbn = request.getParameter("isbn");

            Review review = new Review();
            review.setISBN(isbn);
            review.setRating(rating);
            review.setReviewText(reviewText);
            review.setUserName(userName);
            ReviewDAO reviewDAO = new ReviewDAOImpl();
            boolean registerSuccess = reviewDAO.registerReview(review);
            if (!registerSuccess) {
                throw new Exception("Couldn't register the review");
            }

            response.sendRedirect("BookLookup?isbn=" + isbn);
        } catch (Exception exception) {
            logger.error("Error adding review.", exception);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
    }
}

