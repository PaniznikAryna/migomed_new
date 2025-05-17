package com.migomed.Controller;

import com.migomed.Entity.Worker;
import com.migomed.Service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
// @PreAuthorize можно раскомментировать, если требуется ограничить доступ
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/workers")
public class WorkerController {

    private final WorkerService workerService;

    @Autowired
    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    // Создание нового сотрудника (привязка к пользователю с userId) – только админ
    // @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{userId}")
    public ResponseEntity<Worker> createWorker(@PathVariable Long userId, @RequestBody Worker workerDetails) {
        try {
            Worker createdWorker = workerService.createWorker(userId, workerDetails);
            return ResponseEntity.ok(createdWorker);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Редактирование работника – только админ
    // @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{workerId}")
    public ResponseEntity<Worker> updateWorker(@PathVariable Long workerId, @RequestBody Worker updatedWorker) {
        try {
            Worker worker = workerService.updateWorker(workerId, updatedWorker);
            return ResponseEntity.ok(worker);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Вывод всех работников – доступен всем
    @GetMapping
    public ResponseEntity<List<Worker>> getAllWorkers() {
        return ResponseEntity.ok(workerService.getAllWorkers());
    }

    // Получение работника по ID – доступен всем
    @GetMapping("/{workerId}")
    public ResponseEntity<Worker> getWorkerById(@PathVariable Long workerId) {
        Optional<Worker> workerOpt = workerService.getWorkerById(workerId);
        return workerOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Поиск работников по специализации – доступен всем
    @GetMapping("/search")
    public ResponseEntity<List<Worker>> searchWorkers(@RequestParam String specialization) {
        List<Worker> result = workerService.searchBySpecialization(specialization);
        return ResponseEntity.ok(result);
    }

    // Вывод работников, являющихся администраторами – доступен всем
    @GetMapping("/admins")
    public ResponseEntity<List<Worker>> getAdmins() {
        return ResponseEntity.ok(workerService.getAdmins());
    }

    // Удаление работника – только админ.
    // @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{workerId}")
    public ResponseEntity<Void> deleteWorker(@PathVariable Long workerId) {
        try {
            workerService.deleteWorker(workerId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
