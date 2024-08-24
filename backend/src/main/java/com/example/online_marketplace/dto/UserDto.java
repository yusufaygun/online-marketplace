package com.example.online_marketplace.dto;

import lombok.*;

import java.util.Set;

@Data
public class UserDto {

    private Long id;
    private String username;
    private String name;
    private String surname;
    private String fullName;
    private Set<String> roles;
}
