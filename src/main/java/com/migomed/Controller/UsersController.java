package com.migomed.Controller;

import com.migomed.Entity.Gender;
import com.migomed.Entity.Users;
import com.migomed.Service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    // 1. Получение всех пользователей — только админ
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        return ResponseEntity.ok(usersService.findAll());
    }

    // 2. Получение пользователя по id — сам пользователь или админ
    @PreAuthorize("hasRole('ADMIN') or #id == T(java.lang.Long).parseLong(authentication.name)")
    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        return usersService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 3. Получение списка сотрудников (worker = true) — доступно всем
    // Если требуется, чтобы даже не аутентифицированные могли получить эти данные,
    // убедитесь, что соответствующий эндпоинт указан в permitAll() в SecurityConfig или используйте @PermitAll
    @GetMapping("/employees")
    public ResponseEntity<List<Users>> getEmployees() {
        return ResponseEntity.ok(usersService.findByWorker(true));
    }

    // Получение всех сотрудников с их данными — доступно всем
    @GetMapping("/workers")
    public ResponseEntity<List<Users>> getWorkers() {
        return ResponseEntity.ok(usersService.findWorkers());
    }

    // 4. Поиск пользователей по фамилии — только админ
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<List<Users>> searchBySurname(@RequestParam String surname) {
        return ResponseEntity.ok(usersService.findBySurnameContains(surname));
    }

    // 5. Изменение (обновление) пользователя — только админ
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Long id, @RequestBody Users updatedUser) {
        try {
            Users user = usersService.updateUser(id, updatedUser);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 6. Удаление пользователя — только админ
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<Users> existingUser = usersService.findById(id);
        if (existingUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        usersService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/gender")
    public ResponseEntity<List<Users>> getUsersByGender(@RequestParam Gender gender) {
        return ResponseEntity.ok(usersService.findByGender(gender));
    }
}
