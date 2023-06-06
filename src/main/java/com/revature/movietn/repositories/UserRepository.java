package com.revature.movietn.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.movietn.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * Find user by username.
     * 
     * @param username username string
     * @return an Optional that will contain the User object if found or empty
     *         Optional
     *         otherwise
     */
    public Optional<User> findByUsername(String username);
}
