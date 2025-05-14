package com.migomed.Service;

import com.migomed.Entity.ServiceWorkerEntity;
import com.migomed.Repository.ServiceWorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceWorkerService {

    private final ServiceWorkerRepository serviceWorkerRepository;

    @Autowired
    public ServiceWorkerService(ServiceWorkerRepository serviceWorkerRepository) {
        this.serviceWorkerRepository = serviceWorkerRepository;
    }

    public List<ServiceWorkerEntity> getAllServiceWorkers() {
        return serviceWorkerRepository.findAll();
    }

    public ServiceWorkerEntity createServiceWorker(ServiceWorkerEntity serviceWorkerEntity) {
        return serviceWorkerRepository.save(serviceWorkerEntity);
    }

    public ServiceWorkerEntity updateServiceWorker(Long id, ServiceWorkerEntity updatedEntity) {
        ServiceWorkerEntity existing = serviceWorkerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Запись не найдена"));
        existing.setService(updatedEntity.getService());
        existing.setWorker(updatedEntity.getWorker());
        return serviceWorkerRepository.save(existing);
    }

    public void deleteServiceWorker(Long id) {
        ServiceWorkerEntity existing = serviceWorkerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Запись не найдена"));
        serviceWorkerRepository.delete(existing);
    }

    public Optional<ServiceWorkerEntity> getServiceWorkerById(Long id) {
        return serviceWorkerRepository.findById(id);
    }
}
