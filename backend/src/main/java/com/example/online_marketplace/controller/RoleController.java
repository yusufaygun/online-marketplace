package com.example.online_marketplace.controller;

import com.example.online_marketplace.dto.UserDto;
import com.example.online_marketplace.response.ApiResponse;
import com.example.online_marketplace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/roles")
class RoleController {
    private final UserService userService;

    @Autowired
    public RoleController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/check-role")
    public ApiResponse<String> checkUserRole(Authentication authentication) {
        // Extract the user from the token
        UserDto userDto = userService.findByUsername(authentication.getName());

        // Check if the user has the admin role
        boolean isAdmin = userDto.getRoles().stream()
                .anyMatch(role -> role.equalsIgnoreCase("admin"));

        if (isAdmin) {
            return new ApiResponse<>(true, "Roles retrieved successfully", "admin");
        } else {
            return new ApiResponse<>(true, "Roles retrieved successfully", "user");
        }
    }

}
