package com.revature.movietn.dtos.responses;

import java.time.LocalDate;
import java.util.Set;

import com.revature.movietn.entities.Profile;
import com.revature.movietn.entities.Review;
import com.revature.movietn.entities.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfileResponse {
    private String id;
    private String username;
    private String avatar;
    private LocalDate createdDate;
    private Set<Review> reviews;

    public ProfileResponse(Profile profile) {
        User user = profile.getUser();
        this.id = user.getId();
        this.username = user.getUsername();
        this.avatar = profile.getAvatar();
        this.createdDate = profile.getCreateDate();
        this.reviews = user.getReviews();
    }
}
