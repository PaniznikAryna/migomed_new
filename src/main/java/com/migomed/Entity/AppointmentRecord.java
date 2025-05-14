package com.migomed.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "appointment_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Хранится идентификатор специалиста (из таблицы worker)
    @Column(name = "specialist_id", nullable = false)
    private Long specialistId;

    // Поле для клиента: если клиент есть в БД – его ID (в виде строки), иначе ФИО
    @Column(name = "client_info", nullable = false, length = 255)
    private String clientInfo;

    // Номер телефона
    @Column(name = "phone_number", nullable = false, length = 50)
    private String phoneNumber;

    // Дата приёма (без времени)
    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;
}
