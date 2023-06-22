package com.revature.movietn.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.movietn.dtos.requests.ModifyProfileAvatarRequest;
import com.revature.movietn.dtos.responses.Principal;
import com.revature.movietn.dtos.responses.ProfileResponse;
import com.revature.movietn.services.JwtTokenService;
import com.revature.movietn.services.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    /**
     * User endpoint that handles requests to get user profile information.
     * Validations are made to ensure that user exists and user holds a valid token.
     * 
     * @param userId the user id
     * @param sreq   the HttpServleRequest object containing the auth token
     * @return the ResponseEntity object with a status set to success or failure and
     *         the body containing a Set of MovieListResponse objects
     */
    @GetMapping("/{userId}/profile")
    public ResponseEntity<ProfileResponse> getUserProfile(@PathVariable("userId") String userId,
            @RequestHeader(value = "auth_token", required = true) String token) {

        // get Principal object with user info
        Principal principal = userService.findById(userId);

        // validate token
        jwtTokenService.validateToken(token, principal);

        // get user profile information
        ProfileResponse userResponse = userService.getUserProfile(userId);

        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    /**
     * User endpoint that handles request to modify a user's profile avatar.
     * Validations are made to ensure that user exists and user holds a valid token.
     * 
     * @param req    the ModifyProfileAvatarRequest object mapped from the request
     *               body
     * @param userId the user id
     * @param sreq   the HttpServleRequest object containing the auth token
     * @return the ResponseEntity object with a status set to success or failure and
     *         the body containing a Set of MovieListResponse objects
     */
    @PutMapping("/{userId}/profile/avatar")
    public ResponseEntity<ProfileResponse> updateProfileAvatar(@Valid @RequestBody ModifyProfileAvatarRequest req,
            @PathVariable("userId") String userId,
            @RequestHeader(value = "auth_token", required = true) String token) {

        // get Principal object with user info
        Principal principal = userService.findById(userId);

        // validate token
        jwtTokenService.validateToken(token, principal);

        // modify profile avatar
        ProfileResponse userResponse = userService.updateProfileAvatar(userId, req);

        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }
}
