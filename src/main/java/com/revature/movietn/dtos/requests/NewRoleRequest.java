package com.revature.movietn.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * NewRoleRequest represents a request for creating a new role.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewRoleRequest {
    @NotNull(message = "Role name should not be empty")
    private String name;
}
