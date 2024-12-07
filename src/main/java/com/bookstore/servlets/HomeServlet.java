package com.bookstore.servlets;

import com.bookstore.db.BookDAO;
import com.bookstore.db.BookDAOImpl;
import com.bookstore.models.Book;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class HomeServlet extends HeaderServlet {
    private static final Logger logger = LogManager.getLogger(HomeServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            super.doGet(request, response);
            BookDAO bookDAO = new BookDAOImpl();
            List<Book> books = bookDAO.selectAllBooks();
            request.setAttribute("books", books);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
            dispatcher.forward(request, response);
        } catch (Exception exception) {
            logger.error("Error while trying to display the home page", exception);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
    }
}
