package com.revature.movietn.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewMovieListRequest {
    @NotBlank(message = "Name should not be blank.")
    @Pattern(regexp = "^\\S+(?: \\S+)*$", message = "The name is invalid.")
    private String name;
}
