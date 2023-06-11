package com.revature.movietn.dtos.responses;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

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
    private Set<MovieResponse> movies;

    public MovieListResponse(MovieList movieList) {
        this.id = movieList.getId();

        // transform name to capitalize first letter of every word
        String[] words = movieList.getName().split(" ");
        for (int index = 0; index < words.length; index++) {
            words[index] = StringUtils.capitalize(words[index]);
        }
        movieList.setName(String.join(" ", words));
        this.name = movieList.getName();

        // transform movie set into set of movie responses
        Set<Movie> movies = movieList.getMovies();
        Set<MovieResponse> movieResponses = new HashSet<>();
        for (Movie movie : movies) {

            movieResponses.add(new MovieResponse(movie));
        }
        this.movies = movieResponses;
    }
}
