package com.migomed.Controller;

import com.migomed.Entity.AppointmentRecord;
import com.migomed.Service.AppointmentRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/records")
public class AppointmentRecordController {

    private final AppointmentRecordService recordService;

    @Autowired
    public AppointmentRecordController(AppointmentRecordService recordService) {
        this.recordService = recordService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<AppointmentRecord>> getAllRecords() {
        return ResponseEntity.ok(recordService.getAllRecords());
    }

    @PostMapping
    public ResponseEntity<AppointmentRecord> createRecord(@RequestBody AppointmentRecord record) {
        AppointmentRecord created = recordService.createRecord(record);
        return ResponseEntity.ok(created);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentRecord> updateRecord(@PathVariable Long id, @RequestBody AppointmentRecord record) {
        try {
            AppointmentRecord updated = recordService.updateRecord(id, record);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        try {
            recordService.deleteRecord(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or (#workerId == principal.workerId)")
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentRecord> getRecordById(@PathVariable Long id) {
        Optional<AppointmentRecord> recordOpt = recordService.getRecordById(id);
        return recordOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or (#workerId == principal.workerId)")
    @GetMapping("/worker/{workerId}")
    public ResponseEntity<List<AppointmentRecord>> getRecordsByWorkerId(@PathVariable Long workerId) {
        List<AppointmentRecord> records = recordService.findRecordsByWorkerId(workerId);
        return ResponseEntity.ok(records);
    }
}
