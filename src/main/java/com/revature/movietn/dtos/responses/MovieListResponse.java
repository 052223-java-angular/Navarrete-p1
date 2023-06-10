package com.revature.movietn.dtos.responses;

import com.revature.movietn.entities.MovieList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MovieListResponse {
    private String id;
    private String name;

    public MovieListResponse(MovieList movieList) {
        this.id = movieList.getId();
        this.name = movieList.getName();
    }
}
