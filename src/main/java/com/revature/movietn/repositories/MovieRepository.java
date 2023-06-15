package com.revature.movietn.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.movietn.entities.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {
    public List<Movie> findAll(Sort sort);
}
