package com.revature.movietn.controllers;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.movietn.dtos.requests.GetAllReviewsRequest;
import com.revature.movietn.dtos.requests.ReviewRequest;
import com.revature.movietn.dtos.responses.Principal;
import com.revature.movietn.dtos.responses.ReviewResponse;
import com.revature.movietn.entities.Movie;
import com.revature.movietn.entities.User;
import com.revature.movietn.services.JwtTokenService;
import com.revature.movietn.services.MovieService;
import com.revature.movietn.services.ReviewService;
import com.revature.movietn.services.UserService;
import com.revature.movietn.utils.custom_exceptions.ResourceConflictException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;
    private final MovieService movieService;
    private final JwtTokenService jwtTokenService;

    /**
     * Review endpoint that handles requests to create a review. To create a review
     * the user must exist and must have a valid token. The user must also not have
     * a review already in the db for the same movie. If all these validations check
     * out then a review is created and the movie total votes and total rating are
     * updated.
     * 
     * @param req  the RevewRequest object mapped from request body
     * @param sreq the HttpServletRequest object containing the auth token
     * @return the ResponseEntity object with a status set to success or failure and
     *         body containing the ReviewResponse object
     */
    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody ReviewRequest req, HttpServletRequest sreq) {
        // get token
        String token = sreq.getHeader("auth_token");

        // get user by username
        User foundUser = userService.findByUsername(req.getUsername());

        // validate token
        jwtTokenService.validateToken(token, new Principal(foundUser));

        // check if user already made a review on movie
        if (reviewService.userHasLeftReview(foundUser.getId(), req.getMovieId())) {
            throw new ResourceConflictException("User cannot create multiple reviews for same movie.");
        }

        // get movie by id
        Optional<Movie> foundMovie = movieService.findById(req.getMovieId());
        if (foundMovie.isEmpty()) {
            // create movie
            foundMovie = Optional.of(new Movie(req.getMovieId(), new BigDecimal("0.00"), 0));
        }

        // update movie
        Movie updatedMovie = movieService.updateMovie(foundMovie.get(), req.getRating());

        // save review
        ReviewResponse reviewResponse = reviewService.saveReview(req.getRating(), req.getDescription(), foundUser,
                updatedMovie);

        // respond 201 - OK
        return ResponseEntity.status(HttpStatus.OK).body(reviewResponse);
    }

    /**
     * Reviews endpoint that handles get request to retrieve all reviews for a movie
     * from the database using the movieId. To retrieve reviews the user must exist
     * and must have a valid token.
     * 
     * @param req  the GetAllReviewsRequest object mapped from the request body
     * @param sreq the HttpServletRequest object containing the auth token
     * @return the ResponseEntity object with a status set to success or failure and
     *         the body containing the ReviewResponse object
     */
    @GetMapping
    public ResponseEntity<Set<ReviewResponse>> getAllReviewsForMovie(@Valid @RequestBody GetAllReviewsRequest req,
            HttpServletRequest sreq) {
        // get token
        String token = sreq.getHeader("auth_token");

        // get user by username
        User foundUser = userService.findByUsername(req.getUsername());

        // validate token
        jwtTokenService.validateToken(token, new Principal(foundUser));

        // get all reviews
        Set<ReviewResponse> reviewResponseSet = reviewService.findAllByMovieId(req.getMovieId());

        // respond 201 - OK
        return ResponseEntity.status(HttpStatus.OK).body(reviewResponseSet);
    }
}
