package com.revature.movietn.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.revature.movietn.dtos.requests.DeleteReviewRequest;
import com.revature.movietn.dtos.requests.ModifyReviewRequest;
import com.revature.movietn.dtos.requests.NewReviewRequest;
import com.revature.movietn.dtos.responses.ReviewResponse;
import com.revature.movietn.entities.Movie;
import com.revature.movietn.entities.Review;
import com.revature.movietn.entities.User;
import com.revature.movietn.repositories.ReviewRepository;
import com.revature.movietn.utils.custom_exceptions.BadRequestException;
import com.revature.movietn.utils.custom_exceptions.ResourceNotFoundException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MovieService movieService;

    /**
     * Saves a review to the db.
     * 
     * @param review the Review object
     * @return the ReviewResponse object
     */
    public ReviewResponse saveReview(NewReviewRequest req) {
        // update movie
        movieService.updateMovieWithNewReview(req.getMovieId(), req.getRating());

        // make user and movie
        User user = new User();
        user.setId(req.getUserId());
        Movie movie = new Movie();
        movie.setId(req.getMovieId());
        Review review = new Review(req.getRating(), req.getDescription(), user, movie);

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

    public ReviewResponse findById(String reviewId) {
        Optional<Review> foundReview = reviewRepository.findById(reviewId);
        if (foundReview.isEmpty()) {
            throw new ResourceNotFoundException("Review not found");
        }
        return new ReviewResponse(foundReview.get());
    }

    public ReviewResponse updateReview(ModifyReviewRequest req) {
        // get review from db
        Optional<Review> foundReview = reviewRepository.findById(req.getId());
        if (foundReview.isEmpty()) {
            throw new ResourceNotFoundException("Review not found");
        }
        Review review = foundReview.get();

        // update movie
        movieService.updateMovieWithModifiedReview(req.getMovieId(), review.getRating(), req.getRating());

        // update Review Object
        review.setRating(req.getRating());
        review.setDescription(req.getDescription());

        // update review in db
        return new ReviewResponse(reviewRepository.save(review));
    }

    public void deleteReview(DeleteReviewRequest req) {
        // get review from db
        Optional<Review> foundReview = reviewRepository.findById(req.getId());
        if (foundReview.isEmpty()) {
            throw new ResourceNotFoundException("Review not found");
        }
        Review review = foundReview.get();

        // update movie
        movieService.updateMovieWithDeletedReview(req.getMovieId(), review.getRating());

        // delete review from db
        reviewRepository.deleteById(req.getId());
    }

    public void validateRequestData(String id, String userId, String movieId) {
        Optional<Review> foundReview = reviewRepository.findById(id);
        if (foundReview.isEmpty()) {
            throw new ResourceNotFoundException("Review not found");
        }
        Review review = foundReview.get();

        if (!review.getUser().getId().equals(userId) || !review.getMovie().getId().equals(movieId)) {
            throw new BadRequestException("Request has an invalid data combination.");
        }
    }
}
