package com.revature.movietn.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.revature.movietn.dtos.requests.AddMovieToMovieListRequest;
import com.revature.movietn.dtos.requests.DeleteMovieFromMovieListRequest;
import com.revature.movietn.dtos.requests.DeleteMovieListRequest;
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

    /**
     * Finds a movie list using the movieId. Validates that the movie list belongs
     * to the user using the userId.
     * 
     * @param movieListId the movie list id
     * @param userId      the user id
     * @return the MovieListResponse object
     */
    public MovieListResponse findById(String movieListId, String userId) {
        // get movie list
        Optional<MovieList> foundMovieList = movieListRepository.findById(movieListId);
        if (foundMovieList.isEmpty()) {
            throw new ResourceNotFoundException("Movie list was not found.");
        }
        MovieList movieList = foundMovieList.get();

        // validate user owns movie list
        validateUserOwnsMovieList(movieList, userId);

        return new MovieListResponse(movieList);
    }

    /**
     * Finds all movie lists that belong to a user using the userId.
     * 
     * @param userId the user id
     * @return the Set of MovieListResponse objects
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

    /**
     * Adds a movie to a movie list. If the movie list does not exist in the db then
     * a ResourceNotFoundException is thrown. Request data is validated to ensure
     * that the user owns the movie list before persisting change to db. Once this
     * validation checks out the movie is added to movie list in db.
     * 
     * @param id  the movieListId
     * @param req the AddMovieToMovieListRequest object
     * @return the MovieListResponse object
     */
    public MovieListResponse addMovieToMovieList(String movieListId, AddMovieToMovieListRequest req) {
        // get movie list
        Optional<MovieList> foundMovieList = movieListRepository.findById(movieListId);
        if (foundMovieList.isEmpty()) {
            throw new ResourceNotFoundException("Movie list not found.");
        }
        MovieList movieList = foundMovieList.get();

        // get movie
        MovieResponse movieResponse = movieService.findById(req.getMovieId());
        Movie movie = new Movie(movieResponse.getId(), movieResponse.getTotalRating(), movieResponse.getTotalVotes());

        // validate user owns movie list
        validateUserOwnsMovieList(movieList, req.getUserId());

        // validate movie is in movie list
        if (movieList.getMovies().contains(movie)) {
            throw new BadRequestException("Movie does not belong to movie list");
        }

        // add movie
        Set<Movie> movies = movieList.getMovies();
        movies.add(movie);
        movieList.setMovies(movies);

        // save movie list to db
        return new MovieListResponse(movieListRepository.save(movieList));
    }

    /**
     * Deletes movie list from the db using the movie list id. Before deletion a
     * check is made to ensure that user owns the movie list.
     * 
     * @param movieListId the movieListId
     * @param req         the DelteMovieListRequest object
     */
    public void deleteById(String movieListId, DeleteMovieListRequest req) {
        // get movie list
        Optional<MovieList> foundMovieList = movieListRepository.findById(movieListId);
        if (foundMovieList.isEmpty()) {
            throw new ResourceNotFoundException("Movie list not found.");
        }
        MovieList movieList = foundMovieList.get();

        // validate user owns movie list
        validateUserOwnsMovieList(movieList, req.getUserId());

        // delete movie list
        movieListRepository.delete(movieList);
    }

    /**
     * Deletes movie from movie list. Before deletion a few checks are made: user
     * owns movie list and movie is present in movie list.
     * 
     * @param movieListId the movieListId
     * @param movieId     the movieId
     * @param req         the DeleteMovieFromMovieListRequest object
     */
    public void deleteMovieFromMovieList(String movieListId, String movieId, DeleteMovieFromMovieListRequest req) {
        // get movie list
        Optional<MovieList> foundMovieList = movieListRepository.findById(movieListId);
        if (foundMovieList.isEmpty()) {
            throw new ResourceNotFoundException("Movie list not found.");
        }
        MovieList movieList = foundMovieList.get();

        // get movie
        MovieResponse movieResponse = movieService.findById(movieId);
        Movie movie = new Movie(movieResponse.getId(), movieResponse.getTotalRating(), movieResponse.getTotalVotes());

        // validate user owns movie list
        validateUserOwnsMovieList(movieList, req.getUserId());

        // validate movie is in movie list
        if (!movieList.getMovies().contains(movie)) {
            throw new BadRequestException("Movie does not belong to movie list");
        }

        // delete movie from movie list
        Set<Movie> movies = movieList.getMovies();
        movies.remove(movie);
        movieList.setMovies(movies);

        // save to db
        movieListRepository.save(movieList);
    }

    /*********************** Helper Methods ************************ */
    public void validateUserOwnsMovieList(MovieList movieList, String userId) {
        if (!movieList.getUser().getId().equals(userId)) {
            throw new BadRequestException("User does not own movie list.");
        }
    }
}
