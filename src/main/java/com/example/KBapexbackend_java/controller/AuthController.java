package com.example.KBapexbackend_java.controller;

import com.example.KBapexbackend_java.Repository.UserRepository;
import com.example.KBapexbackend_java.dto.LoginRequest;
import com.example.KBapexbackend_java.dto.LoginResponse;
import com.example.KBapexbackend_java.dto.RegisterRequest;
import com.example.KBapexbackend_java.dto.UserDto;
import com.example.KBapexbackend_java.model.UserModel;
import com.example.KBapexbackend_java.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request.email() == null || request.password() == null) {
            return error(HttpStatus.BAD_REQUEST, "Email and password are required");
        }
        UserModel user = userRepository.findByEmail(request.email());
        if (user == null || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            return error(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new LoginResponse(token, UserDto.from(user)));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (request.email() == null || request.password() == null) {
            return error(HttpStatus.BAD_REQUEST, "Email and password are required");
        }
        if (userRepository.findByEmail(request.email()) != null) {
            return error(HttpStatus.CONFLICT, "A user with that email already exists");
        }
        UserModel user = new UserModel();
        user.setEmail(request.email());
        user.setUsername(request.username() != null ? request.username() : request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(request.role() != null ? request.role() : "USER");
        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new LoginResponse(token, UserDto.from(user)));
    }

    private ResponseEntity<Map<String, String>> error(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of("message", message));
    }
}
