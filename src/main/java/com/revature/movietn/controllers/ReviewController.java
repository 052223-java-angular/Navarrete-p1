package com.revature.movietn.controllers;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revature.movietn.dtos.requests.DeleteReviewRequest;
import com.revature.movietn.dtos.requests.ModifyReviewRequest;
import com.revature.movietn.dtos.requests.NewReviewRequest;
import com.revature.movietn.dtos.responses.Principal;
import com.revature.movietn.dtos.responses.ReviewResponse;
import com.revature.movietn.services.JwtTokenService;
import com.revature.movietn.services.ReviewService;
import com.revature.movietn.services.UserService;
import com.revature.movietn.utils.custom_exceptions.ResourceConflictException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/reviews")
@Validated
@CrossOrigin
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
            @RequestHeader(value = "auth_token", required = true) String token,
            @RequestHeader(value = "userId", required = true) String userId) {

        // get Principal object with user info
        Principal principal = userService.findById(userId);

        // validate token
        jwtTokenService.validateToken(token, principal);

        // check if user already made a review on movie
        if (reviewService.userHasLeftReview(principal.getId(), req.getMovieId())) {
            throw new ResourceConflictException("User cannot create multiple reviews for same movie.");
        }

        // save review
        ReviewResponse reviewResponse = reviewService.saveReview(req, userId);

        // respond 201 - OK
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewResponse);
    }

    /**
     * Review endpoint that handles get request to retrieve all reviews for a movie
     * from the database using the movieId. To retrieve reviews the user must exist
     * and must have a valid token.
     * 
     * @param req     the GetAllReviewsRequest object mapped from the request body
     * @param movieId the movie id
     * @param sreq    the HttpServletRequest object containing the auth token
     * @return the ResponseEntity object with a status set to success or failure and
     *         the body containing the ReviewResponse object
     */
    @GetMapping(params = "movieId")
    public ResponseEntity<Set<ReviewResponse>> getAllReviewsForMovie(
            @RequestParam String movieId,
            @RequestHeader(value = "auth_token", required = true) String token,
            @RequestHeader(value = "userId", required = true) String userId) {

        // get Principal object with user info
        Principal principal = userService.findById(userId);

        // validate token
        jwtTokenService.validateToken(token, principal);

        // get all reviews
        Set<ReviewResponse> reviewResponseSet = reviewService.findAllByMovieId(movieId);

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
     * @param req      the ModifyReviewRequest object mapped from the request body
     * @param reviewId the review id
     * @param sreq     the HttpServletRequest object containing the auth token
     * @return the ResponseEntity object with a status set to success or failure and
     *         the body containing the ReviewResponse object
     */
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(@Valid @RequestBody ModifyReviewRequest req,
            @PathVariable("reviewId") @NotBlank String reviewId,
            @RequestHeader(value = "auth_token", required = true) String token,
            @RequestHeader(value = "userId", required = true) String userId) {

        // get Principal object with user info
        Principal principal = userService.findById(userId);

        // validate token
        jwtTokenService.validateToken(token, principal);

        // validate data belongs to user and is related between each other
        reviewService.validateRequestData(reviewId, userId, req.getMovieId());

        // update review
        ReviewResponse newReview = reviewService.updateReview(reviewId, req);

        return ResponseEntity.status(HttpStatus.OK).body(newReview);
    }

    /**
     * Review endpoint that handles requests to delete a review. To delete a review
     * the user must have authorization. The request data is also validated to
     * ensure that it has a valid combination of data (i.e. Review object in db has
     * matching user id and movie id) to avoid issues where a user alters reviews
     * that don't belong to them or for the incorrect movie.
     * 
     * @param req      the DeleteReviewRequest object mapped from the request body
     * @param reviewId the review id
     * @param sreq     the HttpServletRequest object containing the auth token
     * @return the ResponseEntity object with a status set to success or failure and
     *         the body containing the ReviewResponse object
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@Valid @RequestBody DeleteReviewRequest req,
            @PathVariable("reviewId") @NotBlank String reviewId,
            @RequestHeader(value = "auth_token", required = true) String token,
            @RequestHeader(value = "userId", required = true) String userId) {

        // get Principal object with user info
        Principal principal = userService.findById(userId);

        // validate token
        jwtTokenService.validateToken(token, principal);

        // validate review belongs to user and is related between each other
        reviewService.validateRequestData(reviewId, userId, req.getMovieId());

        // delete review
        reviewService.deleteReview(reviewId, req.getMovieId());

        // respond with 204 -
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Review endpoint that handles get request to retrieve all reviews for a user
     * from the database using the userId. To retrieve reviews the user must exist
     * and must have a valid token.
     * 
     * @param userId the user id
     * @param sreq   the HttpServletRequest object containing the auth token
     * @return the ResponseEntity object with a status set to success or failure and
     *         the body containing the ReviewResponse object
     */
    @GetMapping(params = "userId")
    public ResponseEntity<Set<ReviewResponse>> getAllReviewsForUser(@RequestParam String userId,
            @RequestHeader(value = "auth_token", required = true) String token) {

        // get Principal object with user info
        Principal principal = userService.findById(userId);

        // validate token
        jwtTokenService.validateToken(token, principal);

        // get all reviews
        Set<ReviewResponse> reviewResponseSet = reviewService.findAllByUserId(userId);

        // respond 201 - OK
        return ResponseEntity.status(HttpStatus.OK).body(reviewResponseSet);
    }
}
