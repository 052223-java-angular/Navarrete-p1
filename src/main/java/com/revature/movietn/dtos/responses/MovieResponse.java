package com.revature.movietn.dtos.responses;

import java.math.BigDecimal;

import com.revature.movietn.entities.Movie;

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

    public MovieResponse(Movie movie) {
        this.id = movie.getId();
        this.totalRating = movie.getTotalRating();
        this.totalVotes = movie.getTotalVotes();
    }
}
