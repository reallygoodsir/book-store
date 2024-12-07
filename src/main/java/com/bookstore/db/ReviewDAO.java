package com.bookstore.db;

import com.bookstore.models.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewDAO extends BaseDAO {
    Optional<Review> selectReview(String username, String isbn);

    boolean registerReview(Review review);

    List<Review> getAllReviewsByIsbn(String isbn);

    List<Review> selectReviewsForUser(String userName);

    float selectReviewScore(String isbn);
}
