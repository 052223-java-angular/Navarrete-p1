package com.revature.movietn.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.revature.movietn.dtos.responses.MovieResponse;
import com.revature.movietn.entities.Movie;
import com.revature.movietn.repositories.MovieRepository;
import com.revature.movietn.utils.custom_exceptions.ResourceNotFoundException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class MovieService {
    private final MovieRepository movieRepository;

    /**
     * Finds a movie in the db by movie id.
     * 
     * @param id the movie id
     * @return an Optional object containing the found Movie Object or an empty
     *         Optional object
     */
    public MovieResponse findById(String id) {
        Optional<Movie> foundMovie = movieRepository.findById(id);
        if (foundMovie.isEmpty()) {
            throw new ResourceNotFoundException("Movie not found.");
        }
        return new MovieResponse(foundMovie.get());
    }

    /**
     * Update movie in the db with the new total votes and new total rating
     * 
     * @param id     the movie id string
     * @param rating the new rating recieve from a new review
     * @return the MovieResponse object with updated movie information
     */
    public MovieResponse updateMovieWithNewReview(String id, BigDecimal rating) {
        Optional<Movie> foundMovie = movieRepository.findById(id);
        Movie movie;
        if (foundMovie.isEmpty()) {
            movie = new Movie(id, new BigDecimal("0.00"), 0);
        } else {
            movie = foundMovie.get();
        }

        // update total rating and total votes
        movie.setTotalRating(
                movie.getTotalRating()
                        .multiply(BigDecimal.valueOf(movie.getTotalVotes()))
                        .add(rating)
                        .divide(BigDecimal.valueOf(movie.getTotalVotes() + 1), 2, RoundingMode.CEILING));
        movie.setTotalVotes(movie.getTotalVotes() + 1);

        // update movie in db
        return new MovieResponse(movieRepository.save(movie));
    }

    /**
     * Updates movie total rating when a review was modified. To update the total
     * rating the previous rating needs to be removed and then the new rating needs
     * to be added. total votes does not need to change.
     * 
     * @param id         the review id
     * @param prevRating the previous review rating
     * @param newRating  the new review rating
     * @return the MovieResponse object containing the updated movie information
     */
    public MovieResponse updateMovieWithModifiedReview(String id, BigDecimal prevRating, BigDecimal newRating) {
        Optional<Movie> foundMovie = movieRepository.findById(id);
        if (foundMovie.isEmpty()) {
            throw new ResourceNotFoundException("Movie not found.");
        }
        Movie movie = foundMovie.get();

        // update total rating by removing previous rating and adding new rating
        movie.setTotalRating(
                movie.getTotalRating()
                        .multiply(BigDecimal.valueOf(movie.getTotalVotes()))
                        .subtract(prevRating)
                        .add(newRating)
                        .divide(BigDecimal.valueOf(movie.getTotalVotes()), 2, RoundingMode.CEILING));

        return new MovieResponse(movieRepository.save(movie));
    }

    /**
     * Updates movie total rating and total votes when a review was deleted. To
     * update the total rating the previous rating needs to be removed. total votes
     * is subtracted by one because one review was removed.
     * 
     * @param id     the review id
     * @param rating the rating
     * @return the MovieResponse object containing the updated movie information
     */
    public MovieResponse updateMovieWithDeletedReview(String id, BigDecimal rating) {
        Optional<Movie> foundMovie = movieRepository.findById(id);
        if (foundMovie.isEmpty()) {
            throw new ResourceNotFoundException("Movie not found.");
        }
        Movie movie = foundMovie.get();

        // update total rating by removing previous rating and adding new rating
        movie.setTotalRating(
                movie.getTotalRating()
                        .multiply(BigDecimal.valueOf(movie.getTotalVotes()))
                        .subtract(rating)
                        .divide(BigDecimal.valueOf(movie.getTotalVotes() - 1), 2, RoundingMode.CEILING));
        movie.setTotalVotes(movie.getTotalVotes() - 1);

        return new MovieResponse(movieRepository.save(movie));
    }
}
