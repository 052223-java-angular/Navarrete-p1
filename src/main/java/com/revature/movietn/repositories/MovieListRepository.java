package com.revature.movietn.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.movietn.entities.MovieList;

@Repository
public interface MovieListRepository extends JpaRepository<MovieList, String> {

    /**
     * Finds a movie list using the movie list name and the user id.
     * 
     * @param name   the movie list name
     * @param userId the user id
     * @return the Optional object containing the MovieList if found, otherwise an
     *         empty Optional object
     */
    public Optional<MovieList> findByNameAndUserId(String name, String userId);

    /**
     * Finds all movie lists that belong to a user using the userId.
     * 
     * @param userId the user id
     * @return the Set of MovieList objects
     */
    public Set<MovieList> findAllByUserId(String userId);
}
