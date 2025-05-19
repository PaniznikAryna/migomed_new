package com.migomed.Controller;

import com.migomed.Entity.ServiceEntity;
import com.migomed.Service.ServiceEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/services")
public class ServiceEntityController {

    private final ServiceEntityService serviceEntityService;

    @Autowired
    public ServiceEntityController(ServiceEntityService serviceEntityService) {
        this.serviceEntityService = serviceEntityService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ServiceEntity> createService(@RequestBody ServiceEntity serviceEntity) {
        return ResponseEntity.ok(serviceEntityService.createService(serviceEntity));
    }

    @GetMapping
    public ResponseEntity<List<ServiceEntity>> getAllServices() {
        return ResponseEntity.ok(serviceEntityService.getAllServices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceEntity> getServiceById(@PathVariable Long id) {
        return serviceEntityService.getServiceById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ServiceEntity>> searchBySection(@RequestParam String section) {
        return ResponseEntity.ok(serviceEntityService.searchBySection(section));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ServiceEntity> updateService(@PathVariable Long id, @RequestBody ServiceEntity updatedService) {
        return ResponseEntity.ok(serviceEntityService.updateService(id, updatedService));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceEntityService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}
