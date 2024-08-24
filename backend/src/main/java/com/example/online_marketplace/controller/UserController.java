package com.example.online_marketplace.controller;

import com.example.online_marketplace.dto.UserDto;
import com.example.online_marketplace.response.ApiResponse;
import com.example.online_marketplace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // user adds a seller to blacklist
    @PostMapping("/{userId}/blacklist/{sellerId}")
    public ApiResponse<UserDto> addSellerToBlacklist(@PathVariable Long userId, @PathVariable Long sellerId) {
        UserDto userDto = userService.addSellerToBlacklist(userId, sellerId);
        return new ApiResponse<>(true, "Seller added to blacklist successfully", userDto);
    }

    // user removes a seller from blacklist
    @DeleteMapping("/{userId}/blacklist/{sellerId}")
    public ApiResponse<UserDto> removeSellerFromBlacklist(@PathVariable Long userId, @PathVariable Long sellerId) {
        UserDto userDto = userService.removeSellerFromBlacklist(userId, sellerId);
        return new ApiResponse<>(true, "Seller removed from blacklist successfully", userDto);
    }
}
