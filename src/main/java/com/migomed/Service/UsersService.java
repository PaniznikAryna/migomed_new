package com.migomed.Service;

import com.migomed.DTO.RegisterDTO;
import com.migomed.Entity.Gender;
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

    public Users registerUser(RegisterDTO dto) {
        if (dto.getWorker() == null || dto.getGender() == null) {
            throw new IllegalArgumentException("Поле 'worker' и 'gender' должны быть заданы");
        }

        String hashed = passwordEncoder.encode(dto.getPassportNumber());
        Users user = Users.builder()
                .surname(dto.getSurname())
                .name(dto.getName())
                .patronymic(dto.getPatronymic())
                .passportNumber(hashed)
                .password(hashed)
                .dateOfBirth(dto.getDateOfBirth().toLocalDate())
                .worker(dto.getWorker())
                .gender(dto.getGender()) // Сохранение пола
                .build();
        return usersRepository.save(user);
    }

    public Users updateUser(Long id, Users updatedUser) {
        Optional<Users> existingUserOptional = usersRepository.findById(id);
        if (existingUserOptional.isEmpty()) {
            throw new IllegalArgumentException("Пользователь не найден");
        }

        Users existingUser = existingUserOptional.get();

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
        if (updatedUser.getGender() != null) {
            existingUser.setGender(updatedUser.getGender()); // Обновление пола
        }
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return usersRepository.save(existingUser);
    }

    public List<Users> findByGender(Gender gender) {
        return usersRepository.findByGender(gender);
    }

    public Users loginUserBySurnameAndPassword(String surname, String rawPassword) {
        List<Users> users = usersRepository.findBySurname(surname);

        // Если по фамилии ничего не найдено, считаем, что указана неверная фамилия
        if (users.isEmpty()) {
            return null; // или: throw new UsernameNotFoundException("Неверная фамилия: " + surname);
        }

        // Перебираем всех пользователей с этой фамилией
        for (Users user : users) {
            // Если пароль совпадает, возвращаем пользователя
            if (rawPassword != null && !rawPassword.isEmpty() && passwordEncoder.matches(rawPassword, user.getPassword())) {
                return user;
            }
        }

        // Если ни у одного пользователя пароль не совпал, возвращаем null (или выбрасываем исключение)
        return null; // или: throw new BadCredentialsException("Неверный логин или пароль для фамилии: " + surname);
    }

    public Optional<Users> findById(Long id) {
        return usersRepository.findById(id);
    }

    public List<Users> findAll() {
        return usersRepository.findAll();
    }

    public List<Users> findByWorker(Boolean worker) {
        return usersRepository.findByWorker(worker);
    }

    public List<Users> findWorkers() {
        List<Users> workers = usersRepository.findByWorkerTrue();
        workers.forEach(worker -> worker.setWorkerDetails(workerRepository.findByUser_Id(worker.getId()).orElse(null)));
        return workers;
    }

    public List<Users> findBySurnameContains(String surname) {
        return usersRepository.findBySurnameContainingIgnoreCase(surname);
    }

    public void deleteById(Long id) {
        Optional<Users> userOpt = usersRepository.findById(id);
        if(userOpt.isPresent()){
            Users user = userOpt.get();
            usersRepository.delete(user);
        }
    }
}
