package com.migomed.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Entity
@Table(name = "visits")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Время визита (тип в БД – TIME WITHOUT TIME ZONE)
    @Column(name = "date_visit", nullable = false)
    private LocalTime dateVisit;

    // Текстовое описание приёма или записи
    @Column(name = "appointments", nullable = false)
    private String appointments;

    // Каждый визит связан с одним сотрудником (Worker)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_worker", nullable = false)
    private Worker worker;

    // Каждый визит связан с одним пользователем (Users)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private Users user;
}
