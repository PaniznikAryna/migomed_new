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

    @Column(name = "specialist_id", nullable = false)
    private Long specialistId;

    @Column(name = "client_info", nullable = false, length = 255)
    private String clientInfo;

    @Column(name = "phone_number", nullable = false, length = 50)
    private String phoneNumber;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;
}
