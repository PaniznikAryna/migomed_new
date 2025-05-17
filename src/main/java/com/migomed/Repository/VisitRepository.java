package com.migomed.Repository;

import com.migomed.Entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    List<Visit> findByUser_Id(Long userId);

    List<Visit> findByWorker_Id(Long workerId);
}
