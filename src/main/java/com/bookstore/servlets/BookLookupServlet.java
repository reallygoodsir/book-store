package com.bookstore.servlets;

import com.bookstore.converters.AuthorConverter;
import com.bookstore.converters.GenreConverter;
import com.bookstore.db.*;
import com.bookstore.models.Author;
import com.bookstore.models.Book;
import com.bookstore.models.Genre;
import com.bookstore.models.Review;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class BookLookupServlet extends HeaderServlet {
    private static final Logger logger = LogManager.getLogger(BookLookupServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            super.doGet(request, response);
            String isbn = request.getParameter("isbn");
            HttpSession session = request.getSession();
            logger.info("Get book by isbn [{}]", isbn);
            BookDAO bookDAO = new BookDAOImpl();
            Optional<Book> selectBook = bookDAO.selectBook(isbn);
            if (!selectBook.isPresent()) {
                throw new Exception("Couldn't get a book by isbn " + isbn);
            }
            Book book = selectBook.get();
            request.setAttribute("book", book);

            AuthorsDAO authorsDAO = new AuthorsDAOImpl();
            List<Author> authors = authorsDAO.loadAuthorsByIsbn(isbn);
            AuthorConverter authorConverter = new AuthorConverter();
            String authorNames = authorConverter.convertToAuthorNames(authors);
            request.setAttribute("authorNames", authorNames);
            logger.info("Author names [{}]", authorNames);

            GenreDAO genreDAO = new GenreDAOImpl();
            List<Genre> genres = genreDAO.loadGenresByIsbn(isbn);
            GenreConverter genreConverter = new GenreConverter();
            String genreNames = genreConverter.convertToGenreNames(genres);
            request.setAttribute("genreNames", genreNames);
            logger.info("Genre names [{}]", genreNames);

            ReviewDAO reviewDAO = new ReviewDAOImpl();
            float bookAverageReviewScore = reviewDAO.selectReviewScore(isbn);
            logger.info("Book average review score [{}]", bookAverageReviewScore);
            if (bookAverageReviewScore == -1 || Float.isNaN(bookAverageReviewScore)) {
                request.setAttribute("bookAverageReviewScore", "No Reviews");
            } else {
                request.setAttribute("bookAverageReviewScore", bookAverageReviewScore + " / 5");
            }

            String userName = (String) session.getAttribute("userName");
            if (userName != null && !userName.isEmpty()) {
                Optional<Review> optionalReview = reviewDAO.selectReview(userName, isbn);
                if (optionalReview.isPresent()) {
                    Review userReview = optionalReview.get();
                    request.setAttribute("userReview", userReview);
                    logger.info("Review setAttribute complete | username {} | userReview {}",
                            userName, userReview);
                }
            } else {
                request.setAttribute("userNotLoggedIn", "true");
                logger.info("User isn't logged in");
            }

            List<Review> allReviewsForBook = reviewDAO.getAllReviewsByIsbn(isbn);
            if(!allReviewsForBook.isEmpty()){
                request.setAttribute("allReviewsForBook", allReviewsForBook);
            }
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/BookListing.jsp");
            dispatcher.forward(request, response);
        } catch (Exception exception) {
            logger.error("Error getting the book", exception);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
    }
}