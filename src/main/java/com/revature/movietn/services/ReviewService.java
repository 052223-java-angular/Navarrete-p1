package com.revature.movietn.services;

import org.springframework.stereotype.Service;

import com.revature.movietn.dtos.requests.ReviewRequest;
import com.revature.movietn.entities.Review;
import com.revature.movietn.repositories.ReviewRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;

    public Review saveReview(ReviewRequest req) {
        return null;
    }
}
