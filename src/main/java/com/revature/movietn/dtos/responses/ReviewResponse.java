package com.revature.movietn.dtos.responses;

import java.math.BigDecimal;

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
public class ReviewResponse {
    private String id;
    private BigDecimal rating;
    private String description;

    public ReviewResponse(Review review) {
        this.id = review.getId();
        this.rating = review.getRating();
        this.description = review.getDescription();
    }
}
