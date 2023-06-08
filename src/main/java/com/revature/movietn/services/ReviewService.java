package com.revature.movietn.services;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.revature.movietn.entities.Movie;
import com.revature.movietn.entities.Review;
import com.revature.movietn.entities.User;
import com.revature.movietn.repositories.ReviewRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    /**
     * Saves a review to the db.
     * 
     * @param rating      the review rating
     * @param description the review description
     * @param user        the User object containing information of the user that
     *                    requested the review creation
     * @param movie       the Movie object containing information of the movie being
     *                    reviewed
     * @return the Review object that wsa saved to the db
     */
    public Review saveReview(BigDecimal rating, String description, User user, Movie movie) {
        // save review
        return reviewRepository.save(new Review(rating, description, user, movie));
    }

    /**
     * Checks if the user has left a review by search for review in db using the
     * user id and movie id.
     * 
     * @param userId  the user id
     * @param movieId the movie id
     * @return true if a review was found, otherwise false
     */
    public boolean userHasLeftReview(String userId, String movieId) {
        Optional<Review> foundReview = reviewRepository.findByUserIdAndMovieId(userId, movieId);
        if (foundReview.isPresent()) {
            return true;
        }
        return false;
    }
}
