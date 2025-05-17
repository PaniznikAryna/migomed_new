package com.migomed.Controller;

import com.migomed.Entity.Analysis;
import com.migomed.Service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;

    @Autowired
    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    // Создание нового анализа для указанного сотрудника и пользователя.
    // Пример URL: POST /analysis/{workerId}/{userId}
    @PostMapping("/{workerId}/{userId}")
    public ResponseEntity<Analysis> createAnalysis(@PathVariable Long workerId,
                                                   @PathVariable Long userId,
                                                   @RequestBody Analysis analysis) {
        try {
            Analysis created = analysisService.createAnalysis(workerId, userId, analysis);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Обновление анализа по id.
    @PutMapping("/{id}")
    public ResponseEntity<Analysis> updateAnalysis(@PathVariable Long id,
                                                   @RequestBody Analysis analysis) {
        try {
            Analysis updated = analysisService.updateAnalysis(id, analysis);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Удаление анализа по id.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnalysis(@PathVariable Long id) {
        try {
            analysisService.deleteAnalysis(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Получение анализа по id.
    @GetMapping("/{id}")
    public ResponseEntity<Analysis> getAnalysisById(@PathVariable Long id) {
        Optional<Analysis> analysisOpt = analysisService.getAnalysisById(id);
        return analysisOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Получение списка всех анализов.
    @GetMapping
    public ResponseEntity<List<Analysis>> getAllAnalyses() {
        return ResponseEntity.ok(analysisService.getAllAnalyses());
    }

    // Получение анализов по id пользователя.
    // URL: GET /analysis/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Analysis>> getAnalysesByUserId(@PathVariable Long userId) {
        List<Analysis> analyses = analysisService.getAnalysesByUserId(userId);
        return ResponseEntity.ok(analyses);
    }

    // Получение анализов по id сотрудника.
    // URL: GET /analysis/worker/{workerId}
    @GetMapping("/worker/{workerId}")
    public ResponseEntity<List<Analysis>> getAnalysesByWorkerId(@PathVariable Long workerId) {
        List<Analysis> analyses = analysisService.getAnalysesByWorkerId(workerId);
        return ResponseEntity.ok(analyses);
    }
}
