package com.revature.movietn.controllers;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revature.movietn.dtos.requests.NewMovieListRequest;
import com.revature.movietn.dtos.responses.MovieListResponse;
import com.revature.movietn.dtos.responses.Principal;
import com.revature.movietn.services.JwtTokenService;
import com.revature.movietn.services.MovieListService;
import com.revature.movietn.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/movie-lists")
public class MovieListController {
    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final MovieListService movieListService;

    /**
     * Movie list endpoint that handles get request to retrieve all movie list for a
     * user from the database using the userId. To retrieve movie list the user must
     * exist and must have a valid token.
     * 
     * @param userId the user id
     * @param sreq   the HttpServletRequest object containing the auth token
     * @return the ResponseEntity object with a status set to success or failure and
     *         the body containing a Set of MovieListResponse objects
     */
    @GetMapping(params = "userId")
    public ResponseEntity<Set<MovieListResponse>> getAllMovieListsForUser(@RequestParam String userId,
            HttpServletRequest sreq) {
        // get token
        String token = sreq.getHeader("auth_token");

        // get Principal object with user info
        Principal principal = userService.findById(userId);

        // validate token
        jwtTokenService.validateToken(token, principal);

        // get all movie lists for user
        Set<MovieListResponse> movieListResponses = movieListService.findAllByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(movieListResponses);
    }

    /**
     * Movie list endpoint that handles requests to create a movie list. To create a
     * movie list the user must exist and must have a valid token. The user must
     * also not have a movie list already in the db with the same name. If all these
     * validations check out then a movie list is created.
     * 
     * @param req  the NewMovieListRequest object mapped from request body
     * @param sreq the HttpServletRequest object containing the auth token
     * @return the ResponseEntity object with a status set to success or failure and
     *         body containing the MovieListResponse object
     */
    @PostMapping
    public ResponseEntity<MovieListResponse> createMovieList(@Valid @RequestBody NewMovieListRequest req,
            HttpServletRequest sreq) {
        // get token
        String token = sreq.getHeader("auth_token");

        // get Principal object with user info
        Principal principal = userService.findById(req.getUserId());

        // validate token
        jwtTokenService.validateToken(token, principal);

        // save movie list
        MovieListResponse movieListResponse = movieListService.saveMovieLIst(req);

        return ResponseEntity.status(HttpStatus.OK).body(movieListResponse);
    }
}
