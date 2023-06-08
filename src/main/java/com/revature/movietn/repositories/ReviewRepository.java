package com.revature.movietn.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.movietn.entities.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {
    public Optional<Review> findByUserIdAndMovieId(String userId, String movieId);
}
