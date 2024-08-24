package com.example.online_marketplace.controller;

import com.example.online_marketplace.dto.ProductDto;
import com.example.online_marketplace.dto.SellerDto;
import com.example.online_marketplace.dto.UserDto;
import com.example.online_marketplace.response.ApiResponse;
import com.example.online_marketplace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    private final UserService userService;

    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    // user profile page
    @GetMapping
    public ApiResponse<UserDto> getMyDetails(Authentication authentication) {
        // Authentication objesinden username'i al
        String username = authentication.getName();
        UserDto user = userService.findByUsername(username);

        return new ApiResponse<>(true, "User details retrieved successfully.", user);
    }

    @GetMapping("/favorites")
    public ApiResponse<List<ProductDto>> getFavoriteProducts(Authentication authentication) {
        String username = authentication.getName();
        UserDto userDto = userService.findByUsername(username);
        List<ProductDto> favoriteProducts = userService.getFavoriteProducts(userDto.getId());
        return new ApiResponse<>(true, "Favorite products retrieved successfully", favoriteProducts);
    }

    // user adds a product to favlist
    @PostMapping("/favorites")
    public ApiResponse<UserDto> addProductToFavorites(Authentication authentication, @RequestBody Long productId) {
        String username = authentication.getName();
        UserDto user = userService.findByUsername(username);
        UserDto userDto = userService.addProductToFavorites(user.getId(), productId);
        return new ApiResponse<>(true, "Product added to favorites successfully", userDto);
    }

    // user removes a product from favlist
    @DeleteMapping("/favorites")
    public ApiResponse<UserDto> removeProductFromFavorites(Authentication authentication, @RequestBody Map<String, Long> request) {
        String username = authentication.getName();
        UserDto user = userService.findByUsername(username);
        Long productId = request.get("productId");  // productId'yi map'ten alıyoruz
        UserDto userDto = userService.removeProductFromFavorites(user.getId(), productId);
        return new ApiResponse<>(true, "Product removed from favorites successfully", userDto);
    }

    // user gets blacklist **ID's** to use it when filtering products
    @GetMapping("/blacklistids")
    public ApiResponse<List<Long>> getBlacklistIds(Authentication authentication) {
        String username = authentication.getName();
        UserDto userDto = userService.findByUsername(username);
        List<Long> blackListIds = userService.getBlacklist(userDto.getId()).stream().map(SellerDto::getId).toList();
        return new ApiResponse<>(true, "Blacklisted sellers retrieved successfully", blackListIds);
    }

    // user gets blacklist to show it in its profile page
    @GetMapping("/blacklist")
    public ApiResponse<List<SellerDto>> getBlacklist(Authentication authentication) {
        String username = authentication.getName();
        UserDto userDto = userService.findByUsername(username);
        List<SellerDto> blackList = userService.getBlacklist(userDto.getId());
        return new ApiResponse<>(true, "Blacklisted sellers retrieved successfully", blackList);
    }

    // user adds a seller to blacklist
    @PostMapping("/blacklist")
    public ApiResponse<UserDto> addSellerToBlacklist(Authentication authentication, @RequestBody Long sellerId) {
        String username = authentication.getName();
        UserDto user = userService.findByUsername(username);
        UserDto userDto = userService.addSellerToBlacklist(user.getId(), sellerId);
        return new ApiResponse<>(true, "Seller added to blacklist successfully", userDto);
    }

    // user removes a seller from blacklist
    @DeleteMapping("/blacklist")
    public ApiResponse<UserDto> removeSellerFromBlacklist(Authentication authentication, @RequestBody Map<String, Long> request) {
        String username = authentication.getName();
        UserDto user = userService.findByUsername(username);
        Long sellerId = request.get("sellerId");  // sellerId'yi map'ten alıyoruz
        UserDto userDto = userService.removeSellerFromBlacklist(user.getId(), sellerId);
        return new ApiResponse<>(true, "Seller removed from blacklist successfully", userDto);
    }
}
