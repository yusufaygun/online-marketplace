package com.example.online_marketplace.mapper;

import com.example.online_marketplace.dto.UserDto;
import com.example.online_marketplace.dto.UserInputDto;
import com.example.online_marketplace.model.Role;
import com.example.online_marketplace.model.User;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.stream.Collectors;


@NoArgsConstructor
public class UserMapper {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static User mapInputDtoToEntity(UserInputDto inputDto) {
        User user = new User();
        user.setUsername(inputDto.getUsername());
        // Securely hash the password
        user.setPassword(passwordEncoder.encode(inputDto.getPassword()));
        user.setName(inputDto.getName());
        user.setSurname(inputDto.getSurname());
        user.setActive(true); // Set user as active by default
        return user;
    }

    public static UserDto mapEntityToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setName(user.getName());
        userDto.setSurname(user.getSurname());
        userDto.setFullName(user.getName() + " " + user.getSurname());
        userDto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        return userDto;
    }
}
