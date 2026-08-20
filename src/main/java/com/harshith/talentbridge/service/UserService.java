package com.harshith.talentbridge.service;

import com.harshith.talentbridge.dto.LoginRequest;
import com.harshith.talentbridge.dto.LoginResponse;
import com.harshith.talentbridge.dto.RegisterRequest;
import com.harshith.talentbridge.entity.User;
import com.harshith.talentbridge.repository.UserRepository;
import com.harshith.talentbridge.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service // Tells Spring this class contains the main business logic
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    // Injects all the necessary tools via constructor
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    // Method to register a brand new user
    public String register(RegisterRequest request) {

        // Check if the email already exists in the database
        if (userRepository.existsByEmail(request.getEmail())) {
            return "Email already exists!";
        }

        // Create a new User entity object
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // Encrypt the plain text password using BCrypt before storing
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Assign the role (STUDENT, RECRUITER, or ADMIN)
        user.setRole(request.getRole());

        // Save the user record into PostgreSQL database
        userRepository.save(user);

        return "User Registered Successfully";
    }

    // Method to authenticate user and generate a JWT token
    public LoginResponse login(LoginRequest request) {

        // Step 1: Check if the email and password match what is in the database
        // If wrong, this will automatically throw a BadCredentialsException
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Step 2: Fetch the user's full details (like role and name) from the database
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Step 3: Generate a brand new signed JWT token with email and role inside it
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        // Step 4: Package the token, email, role, and name into a response DTO and return it
        return new LoginResponse(token, user.getEmail(), user.getRole().name(), user.getName());
    }
}