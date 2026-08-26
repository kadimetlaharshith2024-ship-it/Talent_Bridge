package com.harshith.talentbridge.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    // 1. Generate Signing Key from UTF-8 bytes (minimum 256 bits)
    private Key getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 2. Extract Username/Email from Token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 3. Extract Single Claim using resolver function
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claims != null ? claimsResolver.apply(claims) : null;
    }

    // 4. Extract all Claims (Payload)
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    // 5. Generate Token for User with Custom Claims (Role)
    public String generateToken(String email, String role) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", role);
        return createToken(extraClaims, email);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 6. Check if Token is Expired
    private boolean isTokenExpired(String token) {
        Date exp = extractExpiration(token);
        return exp != null && exp.before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 7. Validate Token against User details
    public boolean validateToken(String token, String userEmail) {
        final String extractedEmail = extractUsername(token);
        return (extractedEmail != null && extractedEmail.equals(userEmail) && !isTokenExpired(token));
    }
}