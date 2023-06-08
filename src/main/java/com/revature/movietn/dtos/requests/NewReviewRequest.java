package com.revature.movietn.dtos.requests;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewReviewRequest {
    @NotNull(message = "Rating should not be empty.")
    @PositiveOrZero(message = "Rating must be a positive number.")
    @DecimalMax(value = "10.0", inclusive = true, message = "Rating must not exceed 10.0.")
    @Digits(integer = 2, fraction = 1, message = "Rating format must have max 2 integral digits and 1 fractional.")
    private BigDecimal rating;
    @NotBlank(message = "Description should not be empty.")
    private String description;
    @NotBlank(message = "Username should not be empty.")
    private String username;
    @NotBlank(message = "MovieId should not be empty.")
    private String movieId;
}