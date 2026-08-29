package com.harshith.talentbridge.config;

import com.harshith.talentbridge.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Enable CORS with our custom configuration source
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Permit preflight OPTIONS requests from the browser
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Public Authentication & Error endpoints
                        .requestMatchers("/api/auth/**", "/error").permitAll()

                        // Student endpoints
                        .requestMatchers("/api/student/**").hasAnyAuthority("STUDENT", "ROLE_STUDENT")

                        // Recruiter endpoints
                        .requestMatchers("/api/recruiter/**").hasAnyAuthority("RECRUITER", "ROLE_RECRUITER")

                        // Recruiter Job management
                        .requestMatchers(HttpMethod.POST, "/api/jobs/**").hasAnyAuthority("RECRUITER", "ROLE_RECRUITER")
                        .requestMatchers(HttpMethod.PUT, "/api/jobs/**").hasAnyAuthority("RECRUITER", "ROLE_RECRUITER")
                        .requestMatchers(HttpMethod.PATCH, "/api/jobs/**").hasAnyAuthority("RECRUITER", "ROLE_RECRUITER")
                        .requestMatchers(HttpMethod.DELETE, "/api/jobs/**").hasAnyAuthority("RECRUITER", "ROLE_RECRUITER")
                        .requestMatchers(HttpMethod.GET, "/api/jobs/my-jobs").hasAnyAuthority("RECRUITER", "ROLE_RECRUITER")

                        // Student application actions
                        .requestMatchers("/api/applications/apply/**", "/api/applications/withdraw/**", "/api/applications/my-applications").hasAnyAuthority("STUDENT", "ROLE_STUDENT")

                        // Recruiter applicant review and status actions
                        .requestMatchers("/api/applications/job/**", "/api/applications/*/status").hasAnyAuthority("RECRUITER", "ROLE_RECRUITER")

                        // Allow any authenticated user (Student or Recruiter) to view jobs & feed
                        .requestMatchers(HttpMethod.GET, "/api/jobs/**").authenticated()

                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allowed Frontend Origins
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "https://campus-bridge-frontend-lyart.vercel.app",
                "https://campus-bridge-frontend-czalsnrpf-markethub1.vercel.app"
        ));

        // Allowed HTTP Methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // Allowed Headers
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"));

        // Allow cookies / auth credentials
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}