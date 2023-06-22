package com.revature.movietn.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revature.movietn.dtos.responses.MovieResponse;
import com.revature.movietn.dtos.responses.Principal;
import com.revature.movietn.services.JwtTokenService;
import com.revature.movietn.services.MovieService;
import com.revature.movietn.services.RecommendationService;
import com.revature.movietn.services.UserService;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/movies")
@Validated
@CrossOrigin
public class MovieController {
    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final RecommendationService recommendationService;
    private final MovieService movieService;

    @GetMapping(value = "{movieId}/recommendation", params = "amount")
    public ResponseEntity<Set<MovieResponse>> getMovieRecommendations(
            @RequestParam(required = false, defaultValue = "5") @Min(5) @Max(10) Integer amount,
            @PathVariable("movieId") @NotBlank String movieId,
            @RequestHeader(value = "auth_token", required = true) String token,
            @RequestHeader(value = "userId", required = true) String userId) {

        // get Principal object with user info
        Principal principal = userService.findById(userId);

        // validate token
        jwtTokenService.validateToken(token, principal);

        // get recommendations
        Set<MovieResponse> movieResponses = recommendationService.getMovieRecommendations(movieId, amount,
                userId);

        return ResponseEntity.status(HttpStatus.OK).body(movieResponses);
    }

    @GetMapping(params = "ids")
    public ResponseEntity<Set<MovieResponse>> getMovies(
            @RequestParam(required = true) @NotEmpty List<@NotBlank String> ids,
            @RequestHeader(value = "auth_token", required = true) String token,
            @RequestHeader(value = "userId", required = true) String userId) {

        // get Principal object with user info
        Principal principal = userService.findById(userId);

        // validate token
        jwtTokenService.validateToken(token, principal);

        // get movies
        Set<MovieResponse> movieResponses = movieService.findAll(ids);

        return ResponseEntity.status(HttpStatus.OK).body(movieResponses);
    }
}
