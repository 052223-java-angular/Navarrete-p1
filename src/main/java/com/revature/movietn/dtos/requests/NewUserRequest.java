package com.revature.movietn.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewUserRequest {
    @NotBlank(message = "Username should not be empty")
    private String username;
    @NotBlank(message = "Email should not be empty")
    private String email;
    @NotBlank(message = "Password should not be empty")
    private String password;
    @NotBlank(message = "Confirm password should not be empty")
    private String confirmPassword;
}
