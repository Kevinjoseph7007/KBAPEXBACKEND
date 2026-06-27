package com.example.KBapexbackend_java.dto;

import com.example.KBapexbackend_java.model.UserModel;

public record UserDto(Long id, String username, String email, String role) {
    public static UserDto from(UserModel user) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }
}
