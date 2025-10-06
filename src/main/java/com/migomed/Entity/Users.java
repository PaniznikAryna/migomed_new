package com.migomed.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @Column(nullable = false, length = 100)
    @ToString.Include
    private String surname;

    @Column(nullable = false, length = 100)
    @ToString.Include
    private String name;

    @Column(length = 100)
    private String patronymic;

    @Column(name = "passport_number", nullable = false, length = 64)
    private String passportNumber;

    @Column(name = "date_of_birth", nullable = false)
    @ToString.Include
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    @ToString.Include
    private Boolean worker;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Gender gender; 

    @Column(nullable = false, length = 255)
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Worker workerDetails;
}
