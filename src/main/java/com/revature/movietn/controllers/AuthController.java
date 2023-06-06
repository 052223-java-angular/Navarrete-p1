package com.revature.movietn.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.movietn.dtos.requests.NewUserRequest;
import com.revature.movietn.services.UserService;
import com.revature.movietn.utils.custom_exceptions.ResourceConflictException;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody NewUserRequest req) {
        // check if username is valid
        if (!userService.isValidUsername(req.getUsername())) {
            throw new ResourceConflictException(
                    "Username needs to be 8-20 characters long and can only contain letters, numbers, periods, and underscores. No periods or underscores at the start or end. Cannot have pairs of underscores / periods.");
        }

        // check if username is unique
        if (!userService.isUniqueUsername(req.getUsername())) {
            throw new ResourceConflictException("Username " + req.getUsername() + " already exists.");
        }

        // check if email is valid
        if (!userService.isValidEmail(req.getEmail())) {
            throw new ResourceConflictException("Email " + req.getEmail() + " invalid.");
        }

        // check if password is valid
        if (!userService.isValidPassword(req.getPassword())) {
            throw new ResourceConflictException(
                    "Password must be at least 8 characters long and need to contain at least one number, undercase letter, and uppercase letter and one of the following symbols [@#$%^&+=]. Must not contain any whitespaces.");
        }

        // check if passwords match
        if (!userService.isSamePassword(req.getPassword(), req.getConfirmPassword())) {
            throw new ResourceConflictException("Passwords do not match.");
        }

        // register user
        userService.register(req.getUsername(), req.getEmail(), req.getPassword());

        // return 201 - CREATED
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
