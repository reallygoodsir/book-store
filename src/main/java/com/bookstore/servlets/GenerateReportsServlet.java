package com.bookstore.servlets;

import com.bookstore.db.ReportsDAO;
import com.bookstore.db.ReportsDAOImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class GenerateReportsServlet extends HeaderServlet {
    private static final Logger logger = LogManager.getLogger(GenerateReportsServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            super.doGet(request, response);
            ReportsDAO reportsDAO = new ReportsDAOImpl();
            List<List<String>> weeklySales = reportsDAO.getWeeklySales();
            request.setAttribute("weeklySales", weeklySales);

            List<List<String>> monthlySales = reportsDAO.getMonthlySales();
            request.setAttribute("monthlySales", monthlySales);

            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/Reports.jsp");
            dispatcher.forward(request, response);
        } catch (Exception exception) {
            logger.error("Error while generating reports", exception);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
    }
}

