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

public class BookSearchServlet extends HeaderServlet {
    private static final Logger logger = LogManager.getLogger(BookSearchServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            super.doGet(request, response);
            String query = request.getParameter("search-query");
            BookDAO bookDAO = new BookDAOImpl();
            List<Book> books = bookDAO.searchByQuery(query);
            request.setAttribute("books", books);

            request.getRequestDispatcher("Search.jsp").forward(request, response);
        } catch (Exception exception) {
            logger.error("Error while searching for books", exception);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
    }

}

