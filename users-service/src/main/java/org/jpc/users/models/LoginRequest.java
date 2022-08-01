package org.jpc.users.models;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class LoginRequest {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
