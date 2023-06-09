package com.revature.movietn.controllers;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.movietn.dtos.requests.DeleteReviewRequest;
import com.revature.movietn.dtos.requests.GetAllReviewsRequest;
import com.revature.movietn.dtos.requests.ModifyReviewRequest;
import com.revature.movietn.dtos.requests.NewReviewRequest;
import com.revature.movietn.dtos.responses.Principal;
import com.revature.movietn.dtos.responses.ReviewResponse;
import com.revature.movietn.services.JwtTokenService;
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
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody NewReviewRequest req,
            HttpServletRequest sreq) {
        // get token
        String token = sreq.getHeader("auth_token");

        // get Principal object with user info
        Principal principal = userService.findById(req.getUserId());

        // validate token
        jwtTokenService.validateToken(token, principal);

        // check if user already made a review on movie
        if (reviewService.userHasLeftReview(principal.getId(), req.getMovieId())) {
            throw new ResourceConflictException("User cannot create multiple reviews for same movie.");
        }

        // save review
        ReviewResponse reviewResponse = reviewService.saveReview(req);

        // respond 201 - OK
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewResponse);
    }

    /**
     * Review endpoint that handles get request to retrieve all reviews for a movie
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

        // get Principal object with user info
        Principal principal = userService.findById(req.getUserId());

        // validate token
        jwtTokenService.validateToken(token, principal);

        // get all reviews
        Set<ReviewResponse> reviewResponseSet = reviewService.findAllByMovieId(req.getMovieId());

        // respond 201 - OK
        return ResponseEntity.status(HttpStatus.OK).body(reviewResponseSet);
    }

    /**
     * Review endpoint that handles requests to update a review. To update a review
     * the user must have authorization. The request data is also validated to
     * ensure that it has a valid combination of data (i.e. Review object in db has
     * matching user id and movie id) to avoid issues where a user alters reviews
     * that don't belong to them or for the incorrect movie.
     * 
     * @param req  the ModifyReviewRequest object mapped from the request body
     * @param sreq the HttpServletRequest object containing the auth token
     * @return the ResponseEntity object with a status set to success or failure and
     *         the body containing the ReviewResponse object
     */
    @PutMapping
    public ResponseEntity<ReviewResponse> updateReview(@Valid @RequestBody ModifyReviewRequest req,
            HttpServletRequest sreq) {
        // get token
        String token = sreq.getHeader("auth_token");

        // get Principal object with user info
        Principal principal = userService.findById(req.getUserId());

        // validate token
        jwtTokenService.validateToken(token, principal);

        // validate data belongs to user and is related between each other
        reviewService.validateRequestData(req.getId(), req.getUserId(), req.getMovieId());

        // update review
        ReviewResponse newReview = reviewService.updateReview(req);

        return ResponseEntity.status(HttpStatus.OK).body(newReview);
    }

    /**
     * Review endpoint that handles requests to delete a review. To delete a review
     * the user must have authorization. The request data is also validated to
     * ensure that it has a valid combination of data (i.e. Review object in db has
     * matching user id and movie id) to avoid issues where a user alters reviews
     * that don't belong to them or for the incorrect movie.
     * 
     * @param req  the DeleteReviewRequest object mapped from the request body
     * @param sreq the HttpServletRequest object containing the auth token
     * @return the ResponseEntity object with a status set to success or failure and
     *         the body containing the ReviewResponse object
     */
    @DeleteMapping
    public ResponseEntity<?> deleteReview(@Valid @RequestBody DeleteReviewRequest req, HttpServletRequest sreq) {
        // get token
        String token = sreq.getHeader("auth_token");

        // get Principal object with user info
        Principal principal = userService.findById(req.getUserId());

        // validate token
        jwtTokenService.validateToken(token, principal);

        // validate review belongs to user and is related between each other
        reviewService.validateRequestData(req.getId(), req.getUserId(), req.getMovieId());

        // delete review
        reviewService.deleteReview(req);

        // respond with 204 -
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
