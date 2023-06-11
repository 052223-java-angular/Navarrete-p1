package com.revature.movietn.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.revature.movietn.dtos.requests.AddMovieToMovieLIstRequest;
import com.revature.movietn.dtos.requests.NewMovieListRequest;
import com.revature.movietn.dtos.responses.MovieListResponse;
import com.revature.movietn.dtos.responses.MovieResponse;
import com.revature.movietn.entities.Movie;
import com.revature.movietn.entities.MovieList;
import com.revature.movietn.entities.User;
import com.revature.movietn.repositories.MovieListRepository;
import com.revature.movietn.utils.custom_exceptions.BadRequestException;
import com.revature.movietn.utils.custom_exceptions.ResourceConflictException;
import com.revature.movietn.utils.custom_exceptions.ResourceNotFoundException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class MovieListService {
    private final MovieListRepository movieListRepository;
    private final MovieService movieService;

    /**
     * Saves new movie list to db. Before creating a new movie list there is a
     * validation check to make sure that the movie list doesn't already exist in
     * the db. This check is made to ensure that duplicate movie lists are not
     * created.
     * 
     * @param req the NewMovieListRequest object with movie list information
     * @return the MovieListResponse object
     */
    public MovieListResponse saveMovieLIst(NewMovieListRequest req) {
        // transform name to lowercase
        String name = req.getName().toLowerCase();

        // check if movie lists exists to avoid duplicates
        Optional<MovieList> foundMovieList = movieListRepository.findByNameAndUserId(name,
                req.getUserId());
        if (foundMovieList.isPresent()) {
            throw new ResourceConflictException("User already owns this movie list.");
        }

        // make user
        User user = new User();
        user.setId(req.getUserId());

        // make movie list
        MovieList movieList = new MovieList(name, user);

        // save movie list
        return new MovieListResponse(movieListRepository.save(movieList));
    }

    public MovieListResponse findById(String movieListId, String userId) {
        // get movie list
        Optional<MovieList> foundMovieList = movieListRepository.findById(movieListId);
        if (foundMovieList.isEmpty()) {
            throw new ResourceNotFoundException("Movie list was not found.");
        }
        MovieList movieList = foundMovieList.get();

        // validate request data
        if (!movieList.getUser().getId().equals(userId)) {
            throw new BadRequestException("User does not own move list.");
        }

        return new MovieListResponse(movieList);
    }

    /**
     * Finds all movie lists that belong to a user using the userId.
     * 
     * @param userId the user id
     * @return a Set of MovieListResponse objects
     */
    public Set<MovieListResponse> findAllByUserId(String userId) {
        // get all movie lists for user
        Set<MovieList> movieLists = movieListRepository.findAllByUserId(userId);
        Set<MovieListResponse> movieListResponses = new HashSet<>();

        // transform data into dto set
        for (MovieList movieList : movieLists) {
            movieListResponses.add(new MovieListResponse(movieList));
        }

        // return set
        return movieListResponses;
    }

    public MovieListResponse addMovieToMovieList(String id, AddMovieToMovieLIstRequest req) {
        // get movie list
        Optional<MovieList> foundMovieList = movieListRepository.findById(id);
        if (foundMovieList.isEmpty()) {
            throw new ResourceNotFoundException("Movie list not found.");
        }
        MovieList movieList = foundMovieList.get();

        // get movie
        MovieResponse movieResponse = movieService.findById(req.getMovieId());
        Movie movie = new Movie(movieResponse.getId(), movieResponse.getTotalRating(), movieResponse.getTotalVotes());

        // validate request data
        if (!movieList.getUser().getId().equals(req.getUserId())) {
            throw new BadRequestException("User does not own movie list.");
        }

        // add movie
        Set<Movie> movies = movieList.getMovies();
        movies.add(movie);
        movieList.setMovies(movies);

        // save movie list to db
        return new MovieListResponse(movieListRepository.save(movieList));
    }
}