package com.migomed.Controller;

import com.migomed.DTO.LoginDTO;
import com.migomed.DTO.RegisterDTO;
import com.migomed.Entity.Users;
import com.migomed.Security.JwtUtil;
import com.migomed.Service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsersService usersService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(UsersService usersService, JwtUtil jwtUtil) { // внедряем через конструктор
        this.usersService = usersService;
        this.jwtUtil = jwtUtil;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO registerDTO) {
        Users registeredUser = usersService.registerUser(registerDTO);
        return ResponseEntity.ok("Пользователь зарегистрирован с ID: " + registeredUser.getId());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        Users user = usersService.loginUserBySurnameAndPassword(loginDTO.getSurname(), loginDTO.getPassword());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный логин или пароль");
        }

        List<String> roles;
        if (user.getWorkerDetails() == null) {
            roles = List.of("ROLE_USER");
        } else {
            if (Boolean.TRUE.equals(user.getWorkerDetails().getAdmin())) {
                roles = List.of("ROLE_ADMIN");
            } else {
                roles = List.of("ROLE_DOCTOR");
            }
        }

        String token = jwtUtil.generateToken(String.valueOf(user.getId()), roles);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok("Вы успешно вышли из системы");
    }
}
