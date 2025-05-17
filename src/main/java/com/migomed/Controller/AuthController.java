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

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsersService usersService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(UsersService usersService) {
        this.usersService = usersService;
        this.jwtUtil = new JwtUtil(); // Можно также объявить JwtUtil как Spring-бин
    }

    // Доступ к регистрации разрешён только администраторам (проверка происходит через GrantedAuthorities)
   //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO registerDTO) {
        // Например, администратор регистрирует нового пользователя,
        // используя фамилию и номер паспорта как логин и пароль
        Users registeredUser = usersService.registerUser(registerDTO);
        return ResponseEntity.ok("Пользователь зарегистрирован с ID: " + registeredUser.getId());
    }
    // Эндпоинт логина: вход по фамилии и паролю (паспорт)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        Users user = usersService.loginUserBySurnameAndPassword(loginDTO.getSurname(), loginDTO.getPassword());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный логин или пароль");
        }
        // Генерация JWT-токена (subject – ID пользователя)
        String token = jwtUtil.generateToken(String.valueOf(user.getId()));
        return ResponseEntity.ok(token);
    }

    // Эндпоинт логаута (в stateless-системе logout реализуется на клиенте или через blacklist)
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok("Вы успешно вышли из системы");
    }

}
