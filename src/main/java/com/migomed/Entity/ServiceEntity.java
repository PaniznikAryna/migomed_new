package com.migomed.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "services")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
// Используем только id для сравнения, чтобы не задействовать коллекцию workers
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @Column(name = "title", length = 100, nullable = false)
    @ToString.Include
    private String title;

    @Column(name = "cost", nullable = false)
    @ToString.Include
    private Double cost;

    @ManyToMany
    @JoinTable(
            name = "services_worker",
            joinColumns = @JoinColumn(name = "id_services"),
            inverseJoinColumns = @JoinColumn(name = "id_worker")
    )
    @Builder.Default
    private Set<Worker> workers = new HashSet<>();
}
