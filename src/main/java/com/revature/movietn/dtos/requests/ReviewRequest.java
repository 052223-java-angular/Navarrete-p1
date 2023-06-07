package com.revature.movietn.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewRequest {
    @NotNull
    private int rating;
    @NotNull
    private String description;
    @NotNull
    private String username;
    @NotNull
    private String movieId;
}
