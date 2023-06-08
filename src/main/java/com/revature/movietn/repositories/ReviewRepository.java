package com.revature.movietn.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.movietn.entities.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {
    /**
     * Find a review in the db using the user id and the movie id
     * 
     * @param userId  the user id
     * @param movieId the movie id
     * @return an Optional object that may contain the review or an empty Optional
     *         object
     */
    public Optional<Review> findByUserIdAndMovieId(String userId, String movieId);
}
