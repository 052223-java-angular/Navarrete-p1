package com.revature.movietn.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.movietn.dtos.requests.NewRoleRequest;
import com.revature.movietn.services.RoleService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/role")
public class RoleController {
    private final RoleService roleService;

    /**
     * Creates a new role.
     * 
     * @param req the NewRoleRequest object containing role details
     * @return ResponseEntity with HTTP status code indicating success or
     *         failure to create a new role
     */
    @PostMapping("/create")
    public ResponseEntity<?> createRole(@RequestBody NewRoleRequest req) {
        // check if role exists
        if (!roleService.isUniqueRole(req.getName())) {
            // TODO: throw exception
        }

        // save role
        roleService.saveRole(req.getName());

        // return 201 - CREATED
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
