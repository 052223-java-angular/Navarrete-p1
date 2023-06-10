package com.revature.movietn.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.movietn.entities.MovieList;

@Repository
public interface MovieListRepository extends JpaRepository<MovieList, String> {
    public Optional<MovieList> findByNameAndUserId(String name, String userId);

    public Set<MovieList> findAllByUserId(String userId);
}
