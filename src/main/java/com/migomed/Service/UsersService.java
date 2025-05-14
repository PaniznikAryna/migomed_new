package com.migomed.Service;

import com.migomed.DTO.RegisterDTO;
import com.migomed.Entity.Users;
import com.migomed.Repository.UsersRepository;
import com.migomed.Repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final WorkerRepository workerRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository, WorkerRepository workerRepository) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.workerRepository = workerRepository;
    }

    // Регистрация пользователя с обязательной передачей поля worker
    public Users registerUser(RegisterDTO dto) {
        if (dto.getWorker() == null) {
            throw new IllegalArgumentException("Поле 'worker' должно быть задано");
        }
        // Шифруем паспорт в качестве входных данных для пароля (можно изменить логику, если надо)
        String hashed = passwordEncoder.encode(dto.getPassportNumber());
        Users user = Users.builder()
                .surname(dto.getSurname())
                .name(dto.getName())
                .patronymic(dto.getPatronymic())
                .passportNumber(hashed)
                .password(hashed)
                .dateOfBirth(dto.getDateOfBirth().toLocalDate())
                .worker(dto.getWorker())
                .build();
        return usersRepository.save(user);
    }

    // Логин пользователя по фамилии и паролю
    public Users loginUserBySurnameAndPassword(String surname, String rawPassword) {
        Optional<Users> maybeUser = usersRepository.findBySurname(surname);
        if (maybeUser.isPresent()) {
            Users user = maybeUser.get();
            if (rawPassword == null || rawPassword.isEmpty()) {
                throw new IllegalArgumentException("Пароль не может быть пустым!");
            }
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    // Получение пользователя по id
    public Optional<Users> findById(Long id) {
        return usersRepository.findById(id);
    }

    // Получение списка всех пользователей
    public List<Users> findAll() {
        return usersRepository.findAll();
    }

    // Получение списка пользователей-сотрудников (worker = true)
    public List<Users> findByWorker(Boolean worker) {
        return usersRepository.findByWorker(worker);
    }

    // Получение списка всех сотрудников
    public List<Users> findWorkers() {
        List<Users> workers = usersRepository.findByWorkerTrue();
        workers.forEach(worker -> worker.setWorkerDetails(workerRepository.findByUser_Id(worker.getId()).orElse(null)));
        return workers;
    }

    // Поиск пользователей по частичному совпадению фамилии
    public List<Users> findBySurnameContains(String surname) {
        return usersRepository.findBySurnameContainingIgnoreCase(surname);
    }

    // Обновление пользователя – метод, который сохраняет изменения (update или save)
    public Users updateUser(Long id, Users updatedUser) {
        Optional<Users> existingUserOptional = usersRepository.findById(id);
        if (existingUserOptional.isEmpty()) {
            throw new IllegalArgumentException("Пользователь не найден");
        }
        Users existingUser = existingUserOptional.get();

        // Обновляем только переданные поля
        if (updatedUser.getSurname() != null) {
            existingUser.setSurname(updatedUser.getSurname());
        }
        if (updatedUser.getName() != null) {
            existingUser.setName(updatedUser.getName());
        }
        if (updatedUser.getPatronymic() != null) {
            existingUser.setPatronymic(updatedUser.getPatronymic());
        }
        if (updatedUser.getPassportNumber() != null) {
            existingUser.setPassportNumber(updatedUser.getPassportNumber());
        }
        if (updatedUser.getDateOfBirth() != null) {
            existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
        }
        if (updatedUser.getWorker() != null) {
            existingUser.setWorker(updatedUser.getWorker());
        }
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return usersRepository.save(existingUser);
    }

    // Удаление пользователя по id
    public void deleteById(Long id) {
        Optional<Users> userOpt = usersRepository.findById(id);
        if(userOpt.isPresent()){
            Users user = userOpt.get();
            usersRepository.delete(user);
        }
    }
}
