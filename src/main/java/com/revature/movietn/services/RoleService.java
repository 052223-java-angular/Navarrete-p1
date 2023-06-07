package com.revature.movietn.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.revature.movietn.entities.Role;
import com.revature.movietn.repositories.RoleRepository;
import com.revature.movietn.utils.custom_exceptions.ResourceNotFoundException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RoleService {
    private final RoleRepository roleRepository;

    /**
     * Creates a new role and return it
     * 
     * @param name the name of the role we are creating
     * @return the role that was created
     */
    public Role saveRole(String name) {
        return roleRepository.save(new Role(name));
    }

    /**
     * Checks if a role is unique
     * 
     * @param name the name of the role we are validating
     * @return true if a role was not found, otherwise false
     */
    public boolean isUniqueRole(String name) {
        Optional<Role> foundRole = roleRepository.findByName(name);
        if (foundRole.isEmpty()) {
            return true;
        }

        return false;
    }

    /**
     * Finds the role by using the role name.
     * 
     * @param name the role name
     * @return the role if found
     */
    public Role findByName(String name) {
        Optional<Role> foundRole = roleRepository.findByName(name);
        if (foundRole.isEmpty()) {
            throw new ResourceNotFoundException("Role " + name + " not found.");
        }
        return foundRole.get();
    }
}
