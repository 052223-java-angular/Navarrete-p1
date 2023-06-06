package com.revature.movietn.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.movietn.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    /**
     * Finds a role by its name
     * 
     * @param name the name of the role
     * @return an Optional contianing the Role object if found, otherwise
     *         an empty Optional
     */
    Optional<Role> findByName(String name);
}
