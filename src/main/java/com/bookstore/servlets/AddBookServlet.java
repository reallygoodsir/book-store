package com.bookstore.servlets;

import com.bookstore.converters.BookConverter;
import com.bookstore.db.BookDAO;
import com.bookstore.db.BookDAOImpl;
import com.bookstore.models.Book;
import com.bookstore.validators.BookValidator;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AddBookServlet extends HeaderServlet {
    private static final Logger logger = LogManager.getLogger(BookValidator.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        super.doGet(request, response);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/AddBook.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            super.doPost(request, response);
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (!isMultipart) {
                throw new Exception("Request does not have multipart Content");
            }

            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);

            List<FileItem> fileItems = upload.parseRequest(request);
            if (fileItems == null || fileItems.isEmpty()) {
                throw new Exception("No file items in request");
            }

            BookConverter bookConverter = new BookConverter();
            Map<String, String> userInputFields = bookConverter.convert(fileItems);
            logger.info("User input fields [{}]", userInputFields);
            BookValidator bookValidator = new BookValidator();

            // Validate ISBN
            String isbn = userInputFields.get("isbn");
            boolean validISBN = bookValidator.isValidISBN(isbn);
            if (!validISBN) {
                request.setAttribute("isbnError", "true");
            }

            // Check if ISBN is duplicate
            boolean duplicateISBN = bookValidator.isDuplicateISBN(isbn);
            if (duplicateISBN) {
                request.setAttribute("isbnIsDuplicate", "true");
            }

            // Validate genres
            String genre1 = userInputFields.get("genre1");
            String genre2 = userInputFields.get("genre2");
            boolean duplicateGenres = bookValidator.isDuplicateGenres(genre1, genre2);
            if (duplicateGenres) {
                request.setAttribute("genresAreDuplicate", "true");
            }

            // Validate authors
            String author1 = userInputFields.get("author1");
            String author2 = userInputFields.get("author2");
            String author3 = userInputFields.get("author3");
            boolean duplicateAuthors = bookValidator.isDuplicateAuthors(author1, author2, author3);
            if (duplicateAuthors) {
                request.setAttribute("authorsAreDuplicate", "true");
            }

            // Validate price
            String price = userInputFields.get("price");
            boolean validPrice = bookValidator.isValidPrice(price);
            if (!validPrice) {
                request.setAttribute("priceError", "true");
            }

            // Validate publish date
            String publishDate = userInputFields.get("publishDate");
            boolean validPublishDate = bookValidator.isValidPublishDate(publishDate);
            if (!validPublishDate) {
                request.setAttribute("publishDateError", "true");
            }

            // Validate inventory
            String inventory = userInputFields.get("inventory");
            boolean validInventory = bookValidator.isValidInventory(inventory);
            if (!validInventory) {
                request.setAttribute("inventoryError", "true");
            }

            // If any validation failed, forward back to the form with the attributes
            if (!validISBN || duplicateISBN || duplicateGenres || duplicateAuthors ||
                    !validPrice || !validPublishDate || !validInventory) {
                // Set all form fields back to the request to retain the user's input
                for (Map.Entry<String, String> entry : userInputFields.entrySet()) {
                    if (!entry.getKey().equals("file")) { // Skip file field
                        request.setAttribute(entry.getKey(), entry.getValue());
                    }
                }

                // Forward back to the AddBook.jsp with the error messages
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/AddBook.jsp");
                dispatcher.forward(request, response);
                return;
            }

            // If all validations pass, process the file upload
            boolean fileUploaded = false;
            for (FileItem fileItem : fileItems) {
                if (!fileItem.isFormField()) {
                    // A file was uploaded
                    fileUploaded = true;
                    String filePath = getServletContext().getInitParameter("file-upload");
                    File file = new File(filePath + File.separator + fileItem.getName());
                    logger.info("File will be saved to: " + file.getAbsolutePath());
                    try {
                        fileItem.write(file);
                    } catch (Exception exception) {
                        logger.error("Error saving file", exception);
                        request.setAttribute("fileError", "true");
                        // Preserve all attributes, even the file-related ones
                        for (Map.Entry<String, String> entry : userInputFields.entrySet()) {
                            if (!entry.getKey().equals("file")) {
                                request.setAttribute(entry.getKey(), entry.getValue());
                            }
                        }
                        // Forward back to AddBook.jsp with fileError
                        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/AddBook.jsp");
                        dispatcher.forward(request, response);
                        return;
                    }
                }
            }

            // If no file was uploaded, set the fileError attribute and forward to AddBook.jsp
            if (!fileUploaded) {
                // Preserve all attributes, even the file-related ones
                for (Map.Entry<String, String> entry : userInputFields.entrySet()) {
                    if (!entry.getKey().equals("file")) {
                        request.setAttribute(entry.getKey(), entry.getValue());
                    }
                }
                // Forward back to AddBook.jsp with fileError
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/AddBook.jsp");
                dispatcher.forward(request, response);
                return;
            }

            // Convert the book data and create a new Book object
            Book book = bookConverter.convert(userInputFields);
            BookDAO bookDAO = new BookDAOImpl();
            boolean isBookCreated = bookDAO.create(book);
            if (isBookCreated) {
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/AddBookConfirmation.jsp?success=true");
                dispatcher.forward(request, response);
                return;
            }

            // If book creation failed, forward to confirmation page with failure
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/AddBookConfirmation.jsp?success=false");
            dispatcher.forward(request, response);
        } catch (Exception exception) {
            logger.error("Error adding book", exception);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
    }
}
