package com.example.online_marketplace.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserInputDto implements Serializable {

    @NotNull
    @Size(min = 5, max = 15, message = "Username should be min 5 max 15 character.")
    @Pattern(regexp = "^[a-zA-Z0-9._]*$", message = "Username should consist of only letters, numbers, dots and underscores.")
    private String username;

    @NotNull
    @Size(min = 8, max = 20, message = "Password should be between 8-20 characters.")
    private String password;

    @NotNull
    @Size(min = 2, max = 20, message = "Name should be between 2-20 characters.")
    private String name;

    @NotNull
    @Size(min = 2, max = 20, message = "Surame should be between 2-20 characters.")
    private String surname;
}
