package com.revature.movietn.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.movietn.dtos.requests.LoginUserRequest;
import com.revature.movietn.dtos.requests.NewUserRequest;
import com.revature.movietn.dtos.responses.Principal;
import com.revature.movietn.services.JwtTokenService;
import com.revature.movietn.services.MovieListService;
import com.revature.movietn.services.UserService;
import com.revature.movietn.utils.custom_exceptions.ResourceConflictException;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final MovieListService movieListService;

    /**
     * Register API endpoint that handles registration of a user. Prior to saving a
     * user to the database validations are made against username, email, password,
     * and confirm password. Username is also checked agains the database to ensure
     * that a duplicate user record is not created.
     * 
     * @param req the NewUserRequest object mapped from the request body
     * @return the ResponseEntity object with a status set to success or failure
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody NewUserRequest req) {
        // check if username is valid
        if (!userService.isValidUsername(req.getUsername())) {
            throw new ResourceConflictException(
                    "Username requires 8-20 characters, letters, numbers, and symbols [._]. No symbols at start or end and no pairs of symbols.");
        }

        // check if username is unique
        if (!userService.isUniqueUsername(req.getUsername())) {
            throw new ResourceConflictException("Username " + req.getUsername() + " already exists.");
        }

        // check if email is valid
        if (!userService.isValidEmail(req.getEmail())) {
            throw new ResourceConflictException("Email " + req.getEmail() + "is invalid.");
        }

        // check if password is valid
        if (!userService.isValidPassword(req.getPassword())) {
            throw new ResourceConflictException(
                    "Password requires min 8 characters, 1 number, 1 symbol [!@#$%^&+=], and no spaces.");
        }

        // check if passwords match
        if (!userService.isSamePassword(req.getPassword(), req.getConfirmPassword())) {
            throw new ResourceConflictException("Passwords do not match.");
        }

        // register user
        Principal principal = userService.register(req.getUsername(), req.getEmail(), req.getPassword());

        // set up profile
        userService.saveProfile(principal.getId());

        // set up default movie lists
        movieListService.saveDefaultMovieLists(principal.getId());

        // return 201 - CREATED
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Login API endpoint that handles user login. Once a user is validated
     * successfully through the login method in the service layer a jwt token
     * is created and set to the token field in the principal. This principal
     * is sent back in the response.
     * 
     * @param req the LoginUserRequest object mapped from the request body
     * @return the ResponseEntity object with a status set to success or failure
     */
    @PostMapping("/login")
    public ResponseEntity<Principal> loginUser(@Valid @RequestBody LoginUserRequest req) {
        // login user
        Principal principal = userService.login(req.getUsername(), req.getPassword());

        // create jwt token
        String token = jwtTokenService.generateToken(principal);

        // set token in principal
        principal.setToken(token);

        // response
        return ResponseEntity.status(HttpStatus.OK).body(principal);
    }
}
