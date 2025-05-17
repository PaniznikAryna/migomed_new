package com.migomed.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "worker")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_user", nullable = false)
    private Users user;

    @Column(name = "specialization", length = 100)
    @ToString.Include
    private String specialization;

    @Column(name = "admin", nullable = false)
    @ToString.Include
    private Boolean admin = false;

    @Column(name = "experience")
    @ToString.Include
    private Integer experience;

    @ManyToMany(mappedBy = "workers")
    @Builder.Default
    private Set<ServiceEntity> services = new HashSet<>();

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AppointmentRecord> appointmentRecords;

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Schedule> schedule;
}
