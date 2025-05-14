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
        // Если пользователь не числится как сотрудник, устанавливаем true для последующей привязки
        if (!Boolean.TRUE.equals(user.getWorker())) {
            user.setWorker(true);
        }
        // Если у пользователя уже есть привязка (workerDetails), можно либо отклонить создание,
        // либо просто обновить существующую запись. Здесь предположим, что мы создаем новую.
        if (user.getWorkerDetails() != null) {
            throw new IllegalArgumentException("У пользователя уже существует запись сотрудника");
        }

        // Привязываем новую запись сотрудника к пользователю
        workerDetails.setUser(user);
        Worker worker = workerRepository.save(workerDetails);

        // Обновляем связь на стороне пользователя
        user.setWorkerDetails(worker);
        usersRepository.save(user);

        return worker;
    }


    // Редактирование работника: обновляем только те поля, которые переданы
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

    // Вывод всех работников
    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    // Получение работника по его ID (ID записи worker)
    public Optional<Worker> getWorkerById(Long workerId) {
        return workerRepository.findById(workerId);
    }

    // Поиск работников по специализации (частичное совпадение)
    public List<Worker> searchBySpecialization(String specialization) {
        return workerRepository.findBySpecializationContainingIgnoreCase(specialization);
    }

    // Получение списка работников, являющихся администраторами (admin == true)
    public List<Worker> getAdmins() {
        return workerRepository.findByAdminTrue();
    }

    @Transactional
    public void deleteWorker(Long workerId) {
        // Ищем запись сотрудника по его id
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new IllegalArgumentException("Сотрудник не найден"));

        // Получаем связанного пользователя
        Users user = worker.getUser();
        if (user != null) {
            // Приводим пользователя к состоянию,
            // что он больше не является сотрудником
            user.setWorker(false);
            // Разрываем двустороннюю связь: удаляем ссылку на запись сотрудника
            user.setWorkerDetails(null);
            // Сохраняем изменения в сущности пользователя и сразу сбрасываем их в БД
            usersRepository.saveAndFlush(user);
        }
        // Явно удаляем запись сотрудника
        workerRepository.deleteById(workerId);
    }


}
