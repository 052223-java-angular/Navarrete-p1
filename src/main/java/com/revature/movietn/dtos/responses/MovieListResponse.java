package com.revature.movietn.dtos.responses;

import java.util.Set;

import com.revature.movietn.entities.Movie;
import com.revature.movietn.entities.MovieList;

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
public class MovieListResponse {
    private String id;
    private String name;
    private Set<Movie> movies;

    public MovieListResponse(MovieList movieList) {
        this.id = movieList.getId();
        this.name = movieList.getName();
        this.movies = movieList.getMovies();
    }
}
