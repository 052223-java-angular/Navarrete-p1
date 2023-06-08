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

    public Optional<Movie> findById(String id) {
        Optional<Movie> foundMovie = movieRepository.findById(id);
        if (foundMovie.isEmpty()) {
            return Optional.empty();
        }
        return foundMovie;
    }

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
