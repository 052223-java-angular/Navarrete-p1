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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revature.movietn.dtos.requests.AddMovieToMovieListRequest;
import com.revature.movietn.dtos.requests.NewMovieListRequest;
import com.revature.movietn.dtos.responses.MovieListResponse;
import com.revature.movietn.dtos.responses.Principal;
import com.revature.movietn.services.JwtTokenService;
import com.revature.movietn.services.MovieListService;
import com.revature.movietn.services.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/movie-lists")
@Validated
@CrossOrigin
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
            @RequestHeader(value = "auth_token", required = true) String token) {

        // get Principal object with user info
        Principal principal = userService.findById(userId);

        // validate token
        jwtTokenService.validateToken(token, principal);

        // get all movie lists for user
        Set<MovieListResponse> movieListResponses = movieListService.findAllByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(movieListResponses);
    }

    /**
     * Movie list endpoint that handles requests to get a movie list from the db
     * using the movieId. To retrieve movie list the user must exist and must have a
     * valid token.
     * 
     * @param req         the GetMovieListRequest object mapped from the request
     *                    body
     * @param movieListId the movie list id
     * @param sreq        the HttpServlet object containing the auth token
     * @return the ResponseEntity object with a status set to success or failure and
     *         the body containing a Set of MovieListResponse objects
     */
    @GetMapping("/{movieListId}/movies")
    public ResponseEntity<MovieListResponse> getMovieList(
            @PathVariable("movieListId") @NotBlank String movieListId,
            @RequestHeader(value = "auth_token", required = true) String token,
            @RequestHeader(value = "userId", required = true) String userId) {

        // get Principal object with user info
        Principal principal = userService.findById(userId);

        // validate token
        jwtTokenService.validateToken(token, principal);

        // get movies list with movies
        MovieListResponse movieListResponse = movieListService.findById(movieListId, userId);

        return ResponseEntity.status(HttpStatus.OK).body(movieListResponse);
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
            @RequestHeader(value = "auth_token", required = true) String token,
            @RequestHeader(value = "userId", required = true) String userId) {

        // get Principal object with user info
        Principal principal = userService.findById(userId);

        // validate token
        jwtTokenService.validateToken(token, principal);

        // save movie list
        MovieListResponse movieListResponse = movieListService.saveMovieList(req.getName(), userId);

        return ResponseEntity.status(HttpStatus.OK).body(movieListResponse);
    }

    /**
     * Movie list endpoint that handles requests to add a movie to a movie list. If
     * movie does not exist in the db it is created before adding to movie list.
     * Before this the user is validated to ensure that the user exists and has a
     * valid token. The movie list is also validated to ensure that the movie list
     * in question exists in the db and it belongs to the user. If all these
     * validations check out then the movie is added to the movie list and the
     * controller returns an 200 - OK response with the MovieListResponse object.
     * 
     * @param req         the AddMovieToMovieListRequest object mapped from request
     *                    body
     * @param movieListId the movie list id
     * @param sreq        the HttpServletRequest object containing the auth token
     * @return the ResponseEntity object with a status set to success or failure and
     *         body containing the MovieListResponse object
     */
    @PostMapping("/{movieListId}/movies")
    public ResponseEntity<MovieListResponse> addMovieToMovieList(@Valid @RequestBody AddMovieToMovieListRequest req,
            @PathVariable("movieListId") @NotBlank String movieListId,
            @RequestHeader(value = "auth_token", required = true) String token,
            @RequestHeader(value = "userId", required = true) String userId) {

        // get Principal object with user info
        Principal principal = userService.findById(userId);

        // validate token
        jwtTokenService.validateToken(token, principal);

        // add movie to movie list
        MovieListResponse movieListResponse = movieListService.addMovieToMovieList(movieListId, req.getMovieId(),
                userId);

        return ResponseEntity.status(HttpStatus.OK).body(movieListResponse);
    }

    /**
     * Movie list endpoint that handles requests to delete a movie list. Before
     * deletion user must exist and have a valid token. If the validation checks out
     * then the service handles movie list deletion.
     * 
     * @param req         the DeleteMovieListRequest object mapped from the request
     *                    body
     * @param movieListId the movie list id
     * @param sreq        the HttpServletRequest object containing the auth token
     * @return the ResponseEntity object with a status set to success or failure and
     *         body containing the MovieListResponse object
     */
    @DeleteMapping("/{movieListId}")
    public ResponseEntity<?> deleteMovieList(
            @PathVariable("movieListId") @NotBlank String movieListId,
            @RequestHeader(value = "auth_token", required = true) String token,
            @RequestHeader(value = "userId", required = true) String userId) {

        // get Principal object with user info
        Principal principal = userService.findById(userId);

        // validate token
        jwtTokenService.validateToken(token, principal);

        // delete movie list
        movieListService.deleteById(movieListId, userId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Movie list endpoint that handles request to delete a movie from a movie list.
     * Before deletion user must exist and have a valid token. If the validation
     * checks out then the service handles movie list deletion.
     * 
     * @param req         the DeleteMovieFromMovieListRequest object mapped from the
     *                    request body
     * @param movieListId the movie list id
     * @param movieId     the movie id
     * @param sreq        the HttpServletRequest object containing the auth token
     * @return the ResponseEntity object with a status set to success or failure and
     *         body containing the MovieListResponse object
     */
    @DeleteMapping("/{movieListId}/movies/{movieId}")
    public ResponseEntity<?> deleteMovieFromMovieList(
            @PathVariable("movieListId") @NotBlank String movieListId,
            @PathVariable("movieId") @NotBlank String movieId,
            @RequestHeader(value = "auth_token", required = true) String token,
            @RequestHeader(value = "userId", required = true) String userId) {

        // get Principal object with user info
        Principal principal = userService.findById(userId);

        // validate token
        jwtTokenService.validateToken(token, principal);

        // delete movie from movie list
        movieListService.deleteMovieFromMovieList(movieListId, movieId, userId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
