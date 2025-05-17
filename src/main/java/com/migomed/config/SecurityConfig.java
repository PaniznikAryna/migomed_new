package com.migomed.config;

import com.migomed.Security.CustomUserDetailsService;
import com.migomed.Security.JwtAuthenticationFilter;
import com.migomed.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Создаём JWT фильтр, который будет генерировать и обрабатывать токены
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtUtil, userDetailsService);

        http
                // Отключаем CSRF, поскольку мы работаем в stateless режиме (без сессий)
                .csrf(csrf -> csrf.disable())
                // Устанавливаем статическую стратегию управления сессиями – без состояния
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Разрешаем доступ ко всем маршрутам
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().permitAll())
                // Добавляем JWT фильтр в цепочку перед фильтром аутентификации
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
