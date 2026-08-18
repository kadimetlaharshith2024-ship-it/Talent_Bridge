package com.harshith.talentbridge.controller;

import com.harshith.talentbridge.dto.RegisterRequest;
import com.harshith.talentbridge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        String result = userService.register(request);

        if ("Email already exists!".equals(result)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", result));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", result));
    }
}