package com.revature.movietn.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewUserRequest {
    @NotNull(message = "Username should not be empty")
    private String username;
    @NotNull(message = "Email should not be empty")
    private String email;
    @NotNull(message = "Password should not be empty")
    private String password;
    @NotNull(message = "Confirm password should not be empty")
    private String confirmPassword;
}
