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
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

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
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Permit preflight OPTIONS globally
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Public endpoints
                        .requestMatchers("/api/auth/**", "/error").permitAll()

                        // Role-based endpoints
                        .requestMatchers("/api/student/**").hasAnyAuthority("STUDENT", "ROLE_STUDENT")
                        .requestMatchers("/api/recruiter/**").hasAnyAuthority("RECRUITER", "ROLE_RECRUITER")
                        .requestMatchers(HttpMethod.POST, "/api/jobs/**").hasAnyAuthority("RECRUITER", "ROLE_RECRUITER")
                        .requestMatchers(HttpMethod.PUT, "/api/jobs/**").hasAnyAuthority("RECRUITER", "ROLE_RECRUITER")
                        .requestMatchers(HttpMethod.PATCH, "/api/jobs/**").hasAnyAuthority("RECRUITER", "ROLE_RECRUITER")
                        .requestMatchers(HttpMethod.DELETE, "/api/jobs/**").hasAnyAuthority("RECRUITER", "ROLE_RECRUITER")
                        .requestMatchers(HttpMethod.GET, "/api/jobs/my-jobs").hasAnyAuthority("RECRUITER", "ROLE_RECRUITER")
                        .requestMatchers("/api/applications/apply/**", "/api/applications/withdraw/**", "/api/applications/my-applications").hasAnyAuthority("STUDENT", "ROLE_STUDENT")
                        .requestMatchers("/api/applications/job/**", "/api/applications/*/status").hasAnyAuthority("RECRUITER", "ROLE_RECRUITER")
                        .requestMatchers(HttpMethod.GET, "/api/jobs/**").authenticated()

                        .anyRequest().authenticated()
                )
                // Put CorsFilter first, then JwtAuthenticationFilter
                .addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(List.of(
                "http://localhost:5173",
                "http://localhost:3000",
                "https://*.vercel.app"
        ));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}