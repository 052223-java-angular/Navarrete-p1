package com.revature.movietn.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.movietn.dtos.requests.NewUserRequest;
import com.revature.movietn.services.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody NewUserRequest req) {
        // check if username is valid
        if (!userService.isValidUsername(req.getUsername())) {
            // throw exception
        }

        // check if username is unique
        if (!userService.isUniqueUsername(req.getUsername())) {
            // throw exception
        }

        // check if password is valid
        if (!userService.isValidPassword(req.getPassword())) {
            // throw exception
        }

        // check if passwords match
        if (!userService.isSamePassword(req.getPassword(), req.getConfirmPassword())) {
            // throw exception
        }

        // register user
        userService.register(req.getUsername(), req.getPassword());

        // return 201 - CREATED
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
