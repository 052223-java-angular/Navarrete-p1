package com.revature.movietn.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewUserRequest {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
}
