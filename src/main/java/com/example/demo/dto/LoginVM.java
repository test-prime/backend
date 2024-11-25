package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

/**
 * View Model object for storing a user's credentials.
 */
@Data
@ToString
public class LoginVM {

    @NotNull
    private String username;

    @NotNull
    private String password;

    private boolean rememberMe;
}
