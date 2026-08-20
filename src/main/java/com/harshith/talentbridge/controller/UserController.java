package com.harshith.talentbridge.controller;

import com.harshith.talentbridge.dto.LoginRequest;
import com.harshith.talentbridge.dto.LoginResponse;
import com.harshith.talentbridge.dto.RegisterRequest;
import com.harshith.talentbridge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController // Tells Spring this class handles incoming REST API HTTP requests
@RequestMapping("/api/auth") // Base URL path: all endpoints in this file start with /api/auth
public class UserController {

    @Autowired // Automatically connects the UserService bean
    private UserService userService;

    // Endpoint for Registration: POST http://localhost:8080/api/auth/register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        // Call the service method to register user
        String result = userService.register(request);

        // If email was already taken, send 400 Bad Request status code
        if ("Email already exists!".equals(result)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", result));
        }

        // If successful, send 201 Created status code
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", result));
    }

    // Endpoint for Login: POST http://localhost:8080/api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Validate credentials and get back token + role + name
            LoginResponse response = userService.login(request);

            // Return 200 OK along with the token and user details in JSON format
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // If email or password is wrong, return 401 Unauthorized status code
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password"));
        }
    }
}