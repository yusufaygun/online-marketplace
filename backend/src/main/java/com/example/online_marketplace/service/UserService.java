package com.example.online_marketplace.service;

import com.example.online_marketplace.dto.ProductDto;
import com.example.online_marketplace.dto.SellerDto;
import com.example.online_marketplace.dto.UserDto;
import com.example.online_marketplace.dto.UserInputDto;
import org.springframework.data.domain.Page;

import java.util.Arrays;
import java.util.List;


public interface UserService {

    UserDto saveUser(UserInputDto userInputDto);

    UserDto assignRoleToUser(String username, String roleName);

    UserDto saveAdmin(UserInputDto userInputDto);

    Page<UserDto> findAllUsers(int page, int size);


    UserDto findById(Long id);

    UserDto findByUsername(String username);

    UserDto updateUser(Long id, UserInputDto userInputDto);

    void deleteUser(Long id);


    List<ProductDto> getFavoriteProducts(Long userId);

    UserDto addProductToFavorites(Long userId, Long productId);

    UserDto removeProductFromFavorites(Long userId, Long productId);

    List<SellerDto> getBlacklist(Long userId);

    UserDto addSellerToBlacklist(Long userId, Long sellerId);

    UserDto removeSellerFromBlacklist(Long userId, Long sellerId);



}
