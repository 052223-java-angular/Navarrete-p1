package com.revature.movietn.dtos.responses;

import java.math.BigDecimal;
import java.util.Set;

import com.revature.movietn.entities.Movie;
import com.revature.movietn.entities.Review;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class MovieResponse {
    private String id;
    private BigDecimal totalRating;
    private int totalVotes;
    private Set<Review> reviews;

    public MovieResponse(Movie movie) {
        this.id = movie.getId();
        this.totalRating = movie.getTotalRating();
        this.totalVotes = movie.getTotalVotes();
        this.reviews = movie.getReviews();
    }
}
