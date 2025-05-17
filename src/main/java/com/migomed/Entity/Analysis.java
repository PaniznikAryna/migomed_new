package com.migomed.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "analysis")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Analysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Дата анализа
    @Column(name = "analysis_date", nullable = false)
    private LocalDate analysisDate;

    // Поле, в котором хранится результат анализа
    @Column(name = "result")
    private String result;

    // Каждый анализ связан с одним сотрудником
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", nullable = false)
    private Worker worker;

    // Каждый анализ связан с одним пользователем
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;
}
