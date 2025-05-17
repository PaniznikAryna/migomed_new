package com.migomed.Repository;

import com.migomed.Entity.Users;
import com.migomed.Entity.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    // Теперь этот метод возвращает список, так как фамилии могут повторяться
    List<Users> findBySurname(String surname);

    List<Users> findBySurnameContainingIgnoreCase(String surname);

    List<Users> findByWorker(Boolean worker);

    List<Users> findByWorkerTrue();

    List<Users> findByGender(Gender gender);
}
