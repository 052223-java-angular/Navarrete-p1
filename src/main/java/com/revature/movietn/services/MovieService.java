package com.revature.movietn.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.revature.movietn.entities.Movie;
import com.revature.movietn.repositories.MovieRepository;

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
    public Optional<Movie> findById(String id) {
        Optional<Movie> foundMovie = movieRepository.findById(id);
        if (foundMovie.isEmpty()) {
            return Optional.empty();
        }
        return foundMovie;
    }

    /**
     * Update movie in the db with the new total votes and new total rating
     * 
     * @param movie  the Movie object to be updated
     * @param rating the new rating recieve from a new review
     * @return the updated Movie object with updated information
     */
    public Movie updateMovie(Movie movie, BigDecimal rating) {
        // update total rating and total votes
        movie.setTotalRating(
                movie.getTotalRating()
                        .multiply(BigDecimal.valueOf(movie.getTotalVotes()))
                        .add(rating)
                        .divide(BigDecimal.valueOf(movie.getTotalVotes() + 1), 2, RoundingMode.CEILING));
        movie.setTotalVotes(movie.getTotalVotes() + 1);

        // update movie in db
        return movieRepository.save(movie);
    }
}
