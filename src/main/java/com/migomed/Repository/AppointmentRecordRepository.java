package com.migomed.Repository;

import com.migomed.Entity.AppointmentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AppointmentRecordRepository extends JpaRepository<AppointmentRecord, Long> {
    List<AppointmentRecord> findByClientInfoContainingIgnoreCase(String clientInfo);
}
