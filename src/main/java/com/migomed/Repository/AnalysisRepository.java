package com.migomed.Repository;

import com.migomed.Entity.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalysisRepository extends JpaRepository<Analysis, Long> {

    List<Analysis> findByUser_Id(Long userId);

    List<Analysis> findByWorker_Id(Long workerId);
}
