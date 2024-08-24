package com.example.online_marketplace.controller;

import com.example.online_marketplace.dto.SellerDto;
import com.example.online_marketplace.dto.UserDto;
import com.example.online_marketplace.dto.UserInputDto;
import com.example.online_marketplace.model.Product;
import com.example.online_marketplace.model.Seller;
import com.example.online_marketplace.response.ApiResponse;
import com.example.online_marketplace.service.SellerService;
import com.example.online_marketplace.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/admin")
class AdminController {
    private final UserService userService;
    private final SellerService sellerService;

    @Autowired
    public AdminController(UserService userService, SellerService sellerService) {
        this.userService = userService;
        this.sellerService = sellerService;
    }

    // admin gets all users
    @GetMapping("/users")
    public ApiResponse<Page<UserDto>> findAllUsers(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        return new ApiResponse<>(true, "All users retrieved successfully.",
                userService.findAllUsers(page, size));
    }

    // admin gets a user
    @GetMapping("/users/{id}")
    public ApiResponse<UserDto> findUserById(@PathVariable Long id) {
        UserDto userDto = userService.findById(id);
        return new ApiResponse<>(true, "User is here.", userDto);
    }

    // admin adds new user
    @PostMapping("/users")
    public ApiResponse<UserDto> registerUser(@RequestBody @Valid UserInputDto userInputDto) {
        UserDto userDto = userService.saveUser(userInputDto);
        return new ApiResponse<>(true, "User created successfully", userDto);
    }

    // admin adds new admin
    @PostMapping("/admins")
    public ApiResponse<UserDto> registerAdmin(@RequestBody @Valid UserInputDto userInputDto) {
        UserDto userDto = userService.saveAdmin(userInputDto);
        return new ApiResponse<>(true, "Admin created successfully", userDto);
    }

    // admin updates a user
    @PutMapping("/users/{id}")
    public ApiResponse<UserDto> updateUser(@PathVariable Long id, @RequestBody @Valid UserInputDto userInputDto) {
        UserDto userDto = userService.updateUser(id, userInputDto);
        return new ApiResponse<>(true, "User updated successfully", userDto);
    }

    // admin deletes a user
    @DeleteMapping("/users/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ApiResponse<>(true, "User deleted successfully");
    }

    // admin assigns new role to a user (?)
    @PostMapping("/{username}/roles/{roleName}")
    public ApiResponse<UserDto> assignRoleToUser(@PathVariable String username, @PathVariable String roleName) {
        UserDto userDto = userService.assignRoleToUser(username, roleName);
        return new ApiResponse<>(true, "Role assigned successfully", userDto);
    }

    // admin adds new seller
    @PostMapping("/sellers")
    public ApiResponse<SellerDto> saveSeller(@RequestBody Seller seller) {
        SellerDto savedSeller = sellerService.saveSeller(seller);
        return new ApiResponse<>(true, "Seller saved successfully.", savedSeller);
    }

    // admin updates a seller
    @PutMapping("/sellers/{id}")
    public ApiResponse<SellerDto> updateSeller(@PathVariable Long id, @RequestBody Seller seller) {
        SellerDto updatedSeller = sellerService.updateSeller(id, seller);
        return new ApiResponse<>(true, "Seller updated successfully.", updatedSeller);
    }

    // admin deletes a seller
    @DeleteMapping("/sellers/{id}")
    public ApiResponse<Void> deleteSeller(@PathVariable Long id) {
        sellerService.deleteSeller(id);
        return new ApiResponse<>(true, "Seller deleted successfully.");
    }

    // admin adds a product to seller
    // Mevcut seller'a ürün eklemek için endpoint
    @PostMapping("/sellers/{sellerId}/products")
    public ApiResponse<SellerDto> addProductToSeller(@PathVariable Long sellerId, @RequestBody Product product) {
        SellerDto sellerDto = sellerService.addProductToSeller(sellerId, product);
        return new ApiResponse<>(true, "Product added to seller successfully.", sellerDto);
    }

    // admin updates a product
    @PutMapping("/sellers/{sellerId}/products/{productId}")
    public ApiResponse<SellerDto> updateProduct(@PathVariable Long sellerId, @PathVariable Long productId, @RequestBody Product productDetails) {
        SellerDto sellerDto = sellerService.updateProduct(sellerId, productId, productDetails);
        return new ApiResponse<>(true, "Product updated successfully.", sellerDto);
    }


    // admin deletes a product from seller
    // Mevcut seller'dan ürün çıkarmak için endpoint
    @DeleteMapping("/sellers/{sellerId}/products/{productId}")
    public ApiResponse<SellerDto> removeProductFromSeller(@PathVariable Long sellerId, @PathVariable Long productId) {
        SellerDto sellerDto = sellerService.removeProductFromSeller(sellerId, productId);
        return new ApiResponse<>(true, "Product removed from seller successfully.", sellerDto);
    }
    
}

