package com.harshith.talentbridge.service;

import com.harshith.talentbridge.dto.RegisterRequest;
import com.harshith.talentbridge.enums.Role;
import com.harshith.talentbridge.entity.User;
import com.harshith.talentbridge.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service//this tells the SPRING UserService contains Business Logics
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String register(RegisterRequest request) {//this method registers a new USER

        if (userRepository.existsByEmail(request.getEmail())) {
            return "Email already exists!";
        }//suppose frontend sends name email pass and role then
        //request.getEmail() returns mail and if user already exists Registration Stops

        User user = new User();//Create User Object
        //A new User Object is Created

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );//instead of storing the password user entered
        //SpringSecurity converts it into BCrypt hash something like $2a$10......
        //that hash value is stored in database

        user.setRole(request.getRole());

        userRepository.save(user);//JPA generates the SQL behind the scenes
        //the new user record is stored in the database

        return "User Registered Successfully";
    }

}