package com.bookstore.servlets;

import com.bookstore.db.GenreDAO;
import com.bookstore.db.GenreDAOImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public abstract class HeaderServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(HeaderServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doCall(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doCall(request, response);
    }

    private void doCall(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            GenreDAO genreDAO = new GenreDAOImpl();
            List<String> genreNames = genreDAO.loadAllGenreNames();
            request.setAttribute("allGenreNames", genreNames);
        } catch (Exception exception) {
            logger.error("Error while trying to display the home page", exception);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
    }
}
