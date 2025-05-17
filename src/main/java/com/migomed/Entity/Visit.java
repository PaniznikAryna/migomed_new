package com.migomed.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

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

    // Изменяем тип поля на LocalDate для хранения даты визита
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "date_visit", nullable = false)
    private LocalDate dateVisit;

    @Column(name = "appointments", nullable = false)
    private String appointments;

    // Связь ManyToOne с сущностью Worker (один сотрудник может иметь много визитов)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_worker", nullable = false)
    private Worker worker;

    // Связь ManyToOne с сущностью Users (один пользователь может создавать много визитов)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private Users user;
}
