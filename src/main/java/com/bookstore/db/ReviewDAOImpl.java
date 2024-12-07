package com.bookstore.db;

import com.bookstore.models.Review;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ReviewDAOImpl implements ReviewDAO {
    private static final Logger logger = LogManager.getLogger(ReviewDAOImpl.class);
    private static final String SELECT_REVIEW = "select username, isbn, rating, review_text " +
            "from rating " +
            "where username = ? and isbn = ?";
    private static final String REGISTER_REVIEW = "insert into rating (username, isbn, rating, review_text) " +
            "values(?, ?, ?, ?)";

    private static final String SELECT_REVIEWS = "select username, isbn, rating, review_text " +
            "from rating " +
            "where username = ?";

    private static final String SELECT_REVIEW_AMOUNT_AND_SUM =
            "select " +
                    "count(rating) as reviews_count, " +
                    "sum(rating) as total_reviews_sum " +
                    "from rating " +
                    "where isbn = ?";
    private static final String GET_BOOK_REVIEWS =
            "select username, rating, review_text from rating where isbn = ?";

    public Optional<Review> selectReview(String userName, String isbn) {
        try (
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
                PreparedStatement stmtSelectReview = connection.prepareStatement(SELECT_REVIEW)
        ) {
            stmtSelectReview.setString(1, userName);
            stmtSelectReview.setString(2, isbn);
            ResultSet resultSet = stmtSelectReview.executeQuery();
            if (resultSet.next()) {
                Review review = new Review();
                review.setUserName(resultSet.getString(1));
                review.setISBN(resultSet.getString(2));
                review.setRating(resultSet.getInt(3));
                review.setReviewText(resultSet.getString(4));
                return Optional.of(review);
            }
        } catch (Exception exception) {
            logger.error("Error selecting review for user name [{}] and isbn [{}]",
                    userName, isbn, exception);
        }
        return Optional.empty();
    }

    public boolean registerReview(Review review) {
        try (
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
                PreparedStatement stmtRegisterReview = connection.prepareStatement(REGISTER_REVIEW)
        ) {
            stmtRegisterReview.setString(1, review.getUserName());
            stmtRegisterReview.setString(2, review.getISBN());
            stmtRegisterReview.setInt(3, review.getRating());
            stmtRegisterReview.setString(4, review.getReviewText());
            int registerReviewResult = stmtRegisterReview.executeUpdate();
            if (registerReviewResult != 1) {
                throw new Exception("Couldn't register the review " + review);
            }
            return true;
        } catch (Exception exception) {
            logger.error("Error while trying to register the review ", exception);
            return false;
        }
    }

    public List<Review> getAllReviewsByIsbn(String isbn) {
        try (
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
                PreparedStatement stmtGetBookReviews = connection.prepareStatement(GET_BOOK_REVIEWS)
        ) {
            stmtGetBookReviews.setString(1, isbn);
            ResultSet resultSet = stmtGetBookReviews.executeQuery();
            List<Review> reviews = new ArrayList<>();
            while (resultSet.next()) {
                Review review = new Review();
                review.setISBN(isbn);
                review.setRating(resultSet.getInt("rating"));
                review.setReviewText(resultSet.getString("review_text"));
                review.setUserName(resultSet.getString("username"));
                reviews.add(review);
            }
            return reviews;
        } catch (Exception exception) {
            logger.error("Error while getting all reviews for book {}", isbn, exception);
            return Collections.emptyList();
        }
    }

    public List<Review> selectReviewsForUser(String userName) {
        try (
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
                PreparedStatement stmtSelectReviews = connection.prepareStatement(SELECT_REVIEWS)
        ) {
            stmtSelectReviews.setString(1, userName);
            ResultSet resultSet = stmtSelectReviews.executeQuery();
            List<Review> reviews = new ArrayList<>();
            while (resultSet.next()) {
                Review review = new Review();
                review.setUserName(resultSet.getString(1));
                review.setISBN(resultSet.getString(2));
                review.setRating(resultSet.getInt(3));
                review.setReviewText(resultSet.getString(4));
                reviews.add(review);
            }
            return reviews;
        } catch (Exception exception) {
            logger.error("Error occurred while selecting all reviews from user [{}]", userName, exception);
            return Collections.emptyList();
        }
    }

    public float selectReviewScore(String isbn) {
        try (
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
                PreparedStatement stmtReviewCalculations = connection.prepareStatement(SELECT_REVIEW_AMOUNT_AND_SUM)
        ) {
            stmtReviewCalculations.setString(1, isbn);

            ResultSet resultSet = stmtReviewCalculations.executeQuery();
            if (resultSet.next()) {
                float reviewCount = resultSet.getInt("reviews_count");
                float reviewSum = resultSet.getInt("total_reviews_sum");
                return reviewSum / reviewCount;
            } else {
                return -1;
            }
        } catch (Exception exception) {
            logger.error("Error occurred while getting the average review score for a book  with isbn [{}]",
                    isbn, exception);
            return -1;
        }
    }


}

