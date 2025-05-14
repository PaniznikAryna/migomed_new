package com.migomed.Repository;

import com.migomed.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    // Поиск по паспорту (если понадобится)
    Users findByPassportNumber(String passportNumber);

    // Полное совпадение фамилии
    Optional<Users> findBySurname(String surname);

    // Поиск по частичному совпадению фамилии (игнорируя регистр)
    List<Users> findBySurnameContainingIgnoreCase(String surname);

    // Поиск пользователей-сотрудников (где worker = true)
    List<Users> findByWorker(Boolean worker);

    // Найти пользователей-сотрудников
    List<Users> findByWorkerTrue();
}
