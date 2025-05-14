package com.migomed.Service;

import com.migomed.Entity.ServiceEntity;
import com.migomed.Entity.Worker;
import com.migomed.Repository.ServiceRepository;
import com.migomed.Repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ServiceEntityService {

    private final ServiceRepository serviceRepository;
    private final WorkerRepository workerRepository;

    @Autowired
    public ServiceEntityService(ServiceRepository serviceRepository, WorkerRepository workerRepository) {
        this.serviceRepository = serviceRepository;
        this.workerRepository = workerRepository;
    }

    public List<ServiceEntity> getAllServices() {
        return serviceRepository.findAll();
    }

    public ServiceEntity createService(ServiceEntity serviceEntity) {
        // Обрабатываем переданных работников: идём по копии коллекции, чтобы избежать модификации
        if (serviceEntity.getWorkers() != null && !serviceEntity.getWorkers().isEmpty()) {
            Set<Worker> fullWorkers = new HashSet<>();
            for (Worker worker : new HashSet<>(serviceEntity.getWorkers())) {
                Long workerId = worker.getId();
                if (workerId != null) {
                    Worker fullWorker = workerRepository.findById(workerId)
                            .orElseThrow(() -> new IllegalArgumentException("Работник с id " + workerId + " не найден"));
                    fullWorkers.add(fullWorker);
                }
            }
            serviceEntity.setWorkers(fullWorkers);
        }
        // Сохраняем сущность
        ServiceEntity saved = serviceRepository.save(serviceEntity);
        // После сохранения заменяем коллекцию работников на новую копию, чтобы "отвязать" её от PersistenceContext
        // Это предотвращает дальнейшие модификации в момент сериализации
        saved.setWorkers(new HashSet<>(saved.getWorkers()));
        return saved;
    }



    public ServiceEntity updateService(Long id, ServiceEntity updatedService) {
        ServiceEntity existing = serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Услуга не найдена"));
        existing.setTitle(updatedService.getTitle());
        existing.setCost(updatedService.getCost());
        // Обновляем связь с работниками аналогичным образом:
        if (updatedService.getWorkers() != null && !updatedService.getWorkers().isEmpty()) {
            Set<Worker> fullWorkers = new HashSet<>();
            for (Worker worker : updatedService.getWorkers()) {
                Long workerId = worker.getId();
                if (workerId != null) {
                    Worker fullWorker = workerRepository.findById(workerId)
                            .orElseThrow(() -> new IllegalArgumentException("Работник с id " + workerId + " не найден"));
                    fullWorkers.add(fullWorker);
                }
            }
            existing.setWorkers(fullWorkers);
        }
        return serviceRepository.save(existing);
    }

    public void deleteService(Long id) {
        ServiceEntity existing = serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Услуга не найдена"));
        serviceRepository.delete(existing);
    }

    public Optional<ServiceEntity> getServiceById(Long id) {
        return serviceRepository.findById(id);
    }

    public List<ServiceEntity> searchByTitle(String title) {
        return serviceRepository.findByTitleContainingIgnoreCase(title);
    }
}
