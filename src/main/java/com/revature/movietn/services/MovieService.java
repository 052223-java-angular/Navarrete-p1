package com.revature.movietn.services;

import java.math.BigDecimal;
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

    public Movie saveMovie(BigDecimal totalRating, int totalVotes) {
        return movieRepository.save(new Movie(totalRating, totalVotes));
    }
}
