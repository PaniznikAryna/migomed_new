package com.migomed.Service;

import com.migomed.Entity.Users;
import com.migomed.Entity.Worker;
import com.migomed.Repository.UsersRepository;
import com.migomed.Repository.WorkerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class WorkerService {

    private final WorkerRepository workerRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public WorkerService(WorkerRepository workerRepository, UsersRepository usersRepository) {
        this.workerRepository = workerRepository;
        this.usersRepository = usersRepository;
    }

    public Worker createWorker(Long userId, Worker workerDetails) {
        Optional<Users> userOpt = usersRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new IllegalArgumentException("Пользователь не найден");
        }
        Users user = userOpt.get();
        if (!Boolean.TRUE.equals(user.getWorker())) {
            user.setWorker(true);
        }

        if (user.getWorkerDetails() != null) {
            throw new IllegalArgumentException("У пользователя уже существует запись сотрудника");
        }

        workerDetails.setUser(user);
        Worker worker = workerRepository.save(workerDetails);

        user.setWorkerDetails(worker);
        usersRepository.save(user);

        return worker;
    }

    public Worker updateWorker(Long workerId, Worker updatedWorker) {
        Optional<Worker> workerOpt = workerRepository.findById(workerId);
        if (!workerOpt.isPresent()) {
            throw new IllegalArgumentException("Сотрудник не найден");
        }
        Worker worker = workerOpt.get();
        if (updatedWorker.getSpecialization() != null) {
            worker.setSpecialization(updatedWorker.getSpecialization());
        }
        if (updatedWorker.getAdmin() != null) {
            worker.setAdmin(updatedWorker.getAdmin());
        }
        if (updatedWorker.getExperience() != null) {
            worker.setExperience(updatedWorker.getExperience());
        }
        return workerRepository.save(worker);
    }

    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    public Optional<Worker> getWorkerById(Long workerId) {
        return workerRepository.findById(workerId);
    }

    public List<Worker> searchBySpecialization(String specialization) {
        return workerRepository.findBySpecializationContainingIgnoreCase(specialization);
    }

    public List<Worker> getAdmins() {
        return workerRepository.findByAdminTrue();
    }

    public List<Worker> getNoAdmins() {
        return workerRepository.findByAdminFalse();
    }
    @Transactional
    public void deleteWorker(Long workerId) {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new IllegalArgumentException("Сотрудник не найден"));

        Users user = worker.getUser();
        if (user != null) {
            user.setWorker(false);
            user.setWorkerDetails(null);
            usersRepository.saveAndFlush(user);
        }
        workerRepository.deleteById(workerId);
    }
}
