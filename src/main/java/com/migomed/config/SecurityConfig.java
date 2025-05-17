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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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
        // Создаем JWT фильтр для обработки токенов
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtUtil, userDetailsService);

        http
                // Включаем CORS: он будет использовать настройки из bean corsConfigurationSource()
                .cors().and()
                // Отключаем CSRF, так как мы работаем в stateless режиме
                .csrf(csrf -> csrf.disable())
                // Устанавливаем стратегию управления сессиями - без хранения состояния
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Разрешаем публичный доступ ко всем маршрутам
                .authorizeHttpRequests(authz -> authz.anyRequest().permitAll())
                // Добавляем JWT фильтр в цепочку перед стандартным фильтром аутентификации
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // Bean для глобальной настройки CORS
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Разрешаем запросы со всех доменов для разработки, в продакшн настройте список конкретных доменов!
        configuration.setAllowedOriginPatterns(List.of("*"));
        // Разрешенные HTTP методы
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Разрешаем любые заголовки запроса
        configuration.setAllowedHeaders(List.of("*"));
        // Разрешаем передачу куки HTTP (если требуется)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Применяем настройки ко всем маршрутам
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
