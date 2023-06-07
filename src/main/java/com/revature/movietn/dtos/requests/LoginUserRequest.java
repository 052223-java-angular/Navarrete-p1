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
public class LoginUserRequest {
    @NotNull(message = "Username should not be empty")
    private String username;
    @NotNull(message = "Password should not be empty")
    private String password;
}
