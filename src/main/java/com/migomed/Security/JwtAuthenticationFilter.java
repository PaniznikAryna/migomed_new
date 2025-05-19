package com.migomed.Security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = null;
        final String authHeader = request.getHeader("Authorization");

        // Сначала пытаемся извлечь токен из заголовка Authorization (формат: "Bearer <token>")
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }
        // Если заголовок отсутствует – пробуем извлечь токен из куки с именем "jwt"
        else if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // Если токен не найден или пустой, передаем запрос дальше; для таких пользователей SecurityConfig назначит роль ROLE_GUEST
        if (token == null || token.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = null;
        try {
            username = jwtUtil.extractUsername(token);
        } catch (Exception e) {
            logger.error("Ошибка при извлечении имени пользователя из токена", e);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtUtil.validateToken(token, username)) {
                // Извлекаем claim "roles" из токена
                Claims claims = jwtUtil.extractAllClaims(token);
                List<?> rolesClaim = claims.get("roles", List.class);
                // Преобразуем каждую роль в объект GrantedAuthority
                List<GrantedAuthority> authorities = rolesClaim.stream()
                        .map(role -> new SimpleGrantedAuthority(role.toString()))
                        .collect(Collectors.toList());

                // Создаем объект аутентификации, включающий полученные полномочия (authorities)
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Устанавливаем объект аутентификации в SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
