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
public class DeleteReviewRequest {
    @NotBlank(message = "Id should not be empty.")
    private String id;
    @NotBlank(message = "UserId should not be empty.")
    private String userId;
    @NotBlank(message = "Message should not be empty.")
    private String movieId;
}