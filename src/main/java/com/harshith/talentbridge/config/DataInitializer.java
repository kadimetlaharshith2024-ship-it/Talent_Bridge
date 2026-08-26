package com.harshith.talentbridge.config;

import com.harshith.talentbridge.enums.Role;
import com.harshith.talentbridge.entity.User;
import com.harshith.talentbridge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email:admin@talentbridge.io}")
    private String adminEmail;

    @Value("${app.admin.password:Admin@12345}")
    private String adminPassword;

    @Value("${app.admin.name:System Admin}")
    private String adminName;

    @Override
    public void run(String... args) {
        userRepository.findByEmail(adminEmail).ifPresentOrElse(
                user -> {
                    if (user.getRole() != Role.ADMIN) {
                        user.setRole(Role.ADMIN);
                        userRepository.save(user);
                        log.info("Elevated existing user {} to ADMIN role.", adminEmail);
                    }
                },
                () -> {
                    User superAdmin = User.builder()
                            .name(adminName)
                            .email(adminEmail)
                            .password(passwordEncoder.encode(adminPassword))
                            .role(Role.ADMIN)
                            .enabled(true)
                            .build();
                    userRepository.save(superAdmin);
                    log.info("Initialized Master Super Admin account: {}", adminEmail);
                }
        );
    }
}