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

    public Review saveReview(BigDecimal rating, String description, User user, Movie movie) {
        // save review
        return reviewRepository.save(new Review(rating, description, user, movie));
    }

    public boolean userHasLeftReview(String userId, String movieId) {
        Optional<Review> foundReview = reviewRepository.findByUserIdAndMovieId(userId, movieId);
        if (foundReview.isPresent()) {
            return true;
        }
        return false;
    }
}
