package com.migomed.Repository;

import com.migomed.Entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {

    Optional<Worker> findByUser_Id(Long userId);


    // Поиск по частичному совпадению специализации (игнорируя регистр)
    List<Worker> findBySpecializationContainingIgnoreCase(String specialization);

    // Вывод записей, где admin == true
    List<Worker> findByAdminTrue();
}
