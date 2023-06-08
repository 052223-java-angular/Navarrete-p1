package com.revature.movietn.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.revature.movietn.dtos.responses.ReviewResponse;
import com.revature.movietn.entities.Review;
import com.revature.movietn.repositories.ReviewRepository;
import com.revature.movietn.utils.custom_exceptions.ResourceNotFoundException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    /**
     * Saves a review to the db.
     * 
     * @param review the Review object
     * @return the ReviewResponse object
     */
    public ReviewResponse saveReview(Review review) {
        // save review
        return new ReviewResponse(reviewRepository.save(review));
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

    /**
     * Finds all reviews for a movie using the movie id.
     * 
     * @param movieId the movie id
     * @return a List of ReviewResponse objects if reviews were found, otherwise
     *         throws a
     *         ResourceNotFoundException
     */
    public Set<ReviewResponse> findAllByMovieId(String movieId) {
        Set<Review> foundReviews = reviewRepository.findAllByMovieId(movieId);
        if (foundReviews.isEmpty()) {
            throw new ResourceNotFoundException("No reviews for this movie.");
        }

        // iterate over list and create ReviewResponse list
        Set<ReviewResponse> reviewResponseSet = new HashSet<>();
        for (Review review : foundReviews) {
            reviewResponseSet.add(new ReviewResponse(review));
        }

        return reviewResponseSet;
    }

    public Review findById(String reviewId) {
        Optional<Review> foundReview = reviewRepository.findById(reviewId);
        if (foundReview.isEmpty()) {
            throw new ResourceNotFoundException("Review not found");
        }
        return foundReview.get();
    }
}
