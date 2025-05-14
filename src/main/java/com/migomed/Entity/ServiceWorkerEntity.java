package com.migomed.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "services_worker")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceWorkerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_services", nullable = false)
    private ServiceEntity service;

    @ManyToOne
    @JoinColumn(name = "id_worker", nullable = false)
    private Worker worker;
}
