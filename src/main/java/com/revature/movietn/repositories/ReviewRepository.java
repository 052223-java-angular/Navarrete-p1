package com.revature.movietn.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.movietn.entities.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {
    /**
     * Find a review in the db using the user id and the movie id.
     * 
     * @param userId  the user id
     * @param movieId the movie id
     * @return an Optional object that may contain the review or an empty Optional
     *         object
     */
    public Optional<Review> findByUserIdAndMovieId(String userId, String movieId);

    /**
     * Finds all reviews for a movie using the movie id.
     * 
     * @param movieId the movie id
     * @return a List of Review objects or an empty List
     */
    public Set<Review> findAllByMovieId(String movieId);

    /**
     * Finds all reviews for a user using the user id
     * 
     * @param userId the user id
     * @return a List of Review objects or an empty List
     */
    public Set<Review> findAllByUserId(String userId);

    /******************************************************************
     * Recommendation
     *****************************************************************/
    public List<Review> findAllByMovieId(String movieId, Sort sort);

    List<Review> findByMovieIdAndRatingBetweenAndUserIdNot(String movieId, BigDecimal minRating,
            BigDecimal maxRating, String userId, Sort sort);

    public Page<Review> findAllByUserIdAndMovieIdNot(String userId, String movieId, Pageable pageable);
}
