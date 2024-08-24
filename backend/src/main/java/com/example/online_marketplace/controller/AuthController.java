package com.example.online_marketplace.controller;

import com.example.online_marketplace.dto.UserDto;
import com.example.online_marketplace.dto.UserInputDto;
import com.example.online_marketplace.response.ApiResponse;
import com.example.online_marketplace.security.JwtUtil;
import com.example.online_marketplace.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    // Registration method
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto>> register(@RequestBody @Valid UserInputDto userInputDto) {
        UserDto savedUser = userService.saveUser(userInputDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Registration successful", savedUser));
    }

    // Login method
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody UserInputDto userInputDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userInputDto.getUsername(),
                            userInputDto.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(userDetails);

            // Return JWT in the response body instead of setting it as a cookie
            return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", jwt));

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Invalid username or password", null));
        }
    }
}
