package com.migomed.Controller;

import com.migomed.Entity.Visit;
import com.migomed.Service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/visits")
public class VisitController {

    private final VisitService visitService;

    @Autowired
    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    // Создание нового визита: разрешено администратору или сотруднику (worker) с совпадающим workerId.
    @PreAuthorize("hasRole('ROLE_ADMIN') or (#workerId == principal.workerId)")
    @PostMapping("/{workerId}/{userId}")
    public ResponseEntity<Visit> createVisit(@PathVariable Long workerId,
                                             @PathVariable Long userId,
                                             @RequestBody Visit visit) {
        try {
            Visit created = visitService.createVisit(workerId, userId, visit);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Обновление визита по id: разрешено, если админ или если визит принадлежит сотруднику с workerId, равным principal.workerId.
    @PreAuthorize("hasRole('ROLE_ADMIN') or @visitService.isVisitOwnedByWorker(#id, principal.workerId)")
    @PutMapping("/{id}")
    public ResponseEntity<Visit> updateVisit(@PathVariable Long id,
                                             @RequestBody Visit visit) {
        try {
            Visit updated = visitService.updateVisit(id, visit);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Удаление визита по id: разрешено, если админ или если визит принадлежит сотруднику с совпадающим workerId.
    @PreAuthorize("hasRole('ROLE_ADMIN') or @visitService.isVisitOwnedByWorker(#id, principal.workerId)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVisit(@PathVariable Long id) {
        try {
            visitService.deleteVisit(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Получение визита по id: разрешено, если админ, или если визит принадлежит сотруднику (workerId совпадает) или пользователю (userId совпадает).
    @PreAuthorize("hasRole('ROLE_ADMIN') or @visitService.isVisitOwnedByWorker(#id, principal.workerId) or @visitService.isVisitBelongsToUser(#id, principal.userId)")
    @GetMapping("/{id}")
    public ResponseEntity<Visit> getVisitById(@PathVariable Long id) {
        Optional<Visit> visitOpt = visitService.getVisitById(id);
        return visitOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Получение списка всех визитов: разрешено только для администратора.
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<Visit>> getAllVisits() {
        return ResponseEntity.ok(visitService.getAllVisits());
    }

    // Получение визитов по id пользователя: разрешено, если запрашиваемый userId совпадает с principal.userId,
    // или если пользователь – админ, или если authenticated пользователь является сотрудником.
    @PreAuthorize("hasRole('ROLE_ADMIN') or (#userId == principal.userId) or (principal.workerId != null)")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Visit>> getVisitsByUserId(@PathVariable Long userId) {
        List<Visit> visits = visitService.getVisitsByUserId(userId);
        return ResponseEntity.ok(visits);
    }

    // Получение визитов по id сотрудника: разрешено, если админ или если {workerId} совпадает с principal.workerId.
    @PreAuthorize("hasRole('ROLE_ADMIN') or (#workerId == principal.workerId)")
    @GetMapping("/worker/{workerId}")
    public ResponseEntity<List<Visit>> getVisitsByWorkerId(@PathVariable Long workerId) {
        List<Visit> visits = visitService.getVisitsByWorkerId(workerId);
        return ResponseEntity.ok(visits);
    }
}
