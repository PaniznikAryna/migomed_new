package com.migomed.Repository;

import com.migomed.Entity.ServiceWorkerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceWorkerRepository extends JpaRepository<ServiceWorkerEntity, Long> {
}
