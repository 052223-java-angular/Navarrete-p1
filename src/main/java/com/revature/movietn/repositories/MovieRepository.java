package com.revature.movietn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.movietn.entities.Movie;

public interface MovieRepository extends JpaRepository<Movie, String> {

}
