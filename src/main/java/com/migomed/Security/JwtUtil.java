package com.migomed.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import com.migomed.Entity.Users;
import com.migomed.Service.UsersService;

@Component
public class JwtUtil {

    // Используем фиксированное значение для ключа
    private static final String SECRET = "YourSuperSecretKeyForJWTSigning_ReplaceThisWithARealSecretThatIsLongEnough";
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public static final long EXPIRATION_TIME = 12 * 60 * 60 * 1000L; // 12 часов

    public String generateToken(String subject, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(subject);
        claims.put("roles", roles);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean validateToken(String token, String subject) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getSubject().equals(subject)
                    && claims.getExpiration().after(new Date());
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Optional<Users> getUserFromToken(String token, UsersService userService) {
        try {
            String subject = extractUsername(token);
            Long userId = Long.parseLong(subject);
            return userService.findById(userId);
        } catch(Exception e) {
            return Optional.empty();
        }
    }
}
