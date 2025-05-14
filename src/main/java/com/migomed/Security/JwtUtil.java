package com.migomed.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Optional;
import com.migomed.Entity.Users;
import com.migomed.Service.UsersService;

@Component
public class JwtUtil {
    // Генерируем секретный ключ для HS256.
    // В production используйте конфигурируемый/постоянный ключ.
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Время истечения токена: 12 часов (в миллисекундах)
    private static final long EXPIRATION_TIME = 12 * 60 * 60 * 1000L;

    // Генерирует токен, в subject передаём, например, идентификатор пользователя (String)
    public String generateToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    // Извлекает subject (например, userId) из токена
    public String extractUsername(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // Общая валидация токена
    public boolean validateToken(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Валидация токена с проверкой subject и срока действия
    public boolean validateToken(String token, String subject) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject().equals(subject)
                    && claims.getExpiration().after(new Date());
        } catch(Exception ex) {
            return false;
        }
    }

    // Пример получения пользователя на основании токена и сервиса пользователей.
    public Optional<Users> getUserFromToken(String token, UsersService userService) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            String subject = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            Long userId = Long.parseLong(subject);
            return userService.findById(userId);
        } catch(Exception e) {
            return Optional.empty();
        }
    }
}
