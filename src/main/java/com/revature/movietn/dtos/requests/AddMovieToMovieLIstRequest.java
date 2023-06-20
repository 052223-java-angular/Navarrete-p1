package com.revature.movietn.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddMovieToMovieListRequest {
    @NotBlank(message = "MovieId should not be blank.")
    private String movieId;
}
