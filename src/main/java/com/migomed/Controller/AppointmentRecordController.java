package com.migomed.Controller;

import com.migomed.Entity.AppointmentRecord;
import com.migomed.Service.AppointmentRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/records")
public class AppointmentRecordController {

    private final AppointmentRecordService recordService;

    @Autowired
    public AppointmentRecordController(AppointmentRecordService recordService) {
        this.recordService = recordService;
    }

    // 1. Вывод всех записей – только админ
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<AppointmentRecord>> getAllRecords() {
        return ResponseEntity.ok(recordService.getAllRecords());
    }

    // 2. Создание записи – доступно всем
    @PostMapping
    public ResponseEntity<AppointmentRecord> createRecord(@RequestBody AppointmentRecord record) {
        AppointmentRecord created = recordService.createRecord(record);
        return ResponseEntity.ok(created);
    }

    // 3. Редактирование записи – только админ
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentRecord> updateRecord(@PathVariable Long id, @RequestBody AppointmentRecord record) {
        try {
            AppointmentRecord updated = recordService.updateRecord(id, record);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 4. Удаление записи – только админ
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        try {
            recordService.deleteRecord(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 5. Вывод записи по ID – только админ
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentRecord> getRecordById(@PathVariable Long id) {
        Optional<AppointmentRecord> recordOpt = recordService.getRecordById(id);
        return recordOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 6. Поиск по ФИО специалиста – только админ
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search/specialist")
    public ResponseEntity<List<AppointmentRecord>> searchBySpecialist(@RequestParam String fio) {
        List<AppointmentRecord> records = recordService.searchBySpecialistFio(fio);
        return ResponseEntity.ok(records);
    }

    // 7. Поиск по ФИО клиента – доступно администратору и самому клиенту
    // Если пользователь не админ, то возвращаются только записи, где clientInfo совпадает с его ID (username)
    @PreAuthorize("hasRole('ADMIN') or hasAnyRole('CLIENT')")
    @GetMapping("/search/client")
    public ResponseEntity<List<AppointmentRecord>> searchByClient(@RequestParam String fio, Authentication authentication) {
        List<AppointmentRecord> matching = recordService.searchByClientFio(fio);
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            String authId = authentication.getName(); // здесь username – это ID клиента в виде строки
            matching = matching.stream()
                    .filter(r -> r.getClientInfo().equals(authId))
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(matching);
    }
}
