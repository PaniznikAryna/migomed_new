package com.migomed.Controller;

import com.migomed.Entity.Analysis;
import com.migomed.Service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ROLE_ADMIN') or (#workerId == principal.workerId)")
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

    @PreAuthorize("hasRole('ROLE_ADMIN') or @analysisService.isAnalysisOwnedByWorker(#id, principal.workerId)")
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

    @PreAuthorize("hasRole('ROLE_ADMIN') or @analysisService.isAnalysisOwnedByWorker(#id, principal.workerId)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnalysis(@PathVariable Long id) {
        try {
            analysisService.deleteAnalysis(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @analysisService.isAnalysisOwnedByWorker(#id, principal.workerId) or @analysisService.isAnalysisBelongsToUser(#id, principal.userId)")
    @GetMapping("/{id}")
    public ResponseEntity<Analysis> getAnalysisById(@PathVariable Long id) {
        Optional<Analysis> analysisOpt = analysisService.getAnalysisById(id);
        return analysisOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<Analysis>> getAllAnalyses() {
        return ResponseEntity.ok(analysisService.getAllAnalyses());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or (#userId == principal.userId) or (principal.workerId != null)")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Analysis>> getAnalysesByUserId(@PathVariable Long userId) {
        List<Analysis> analyses = analysisService.getAnalysesByUserId(userId);
        return ResponseEntity.ok(analyses);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or (#workerId == principal.workerId)")
    @GetMapping("/worker/{workerId}")
    public ResponseEntity<List<Analysis>> getAnalysesByWorkerId(@PathVariable Long workerId) {
        List<Analysis> analyses = analysisService.getAnalysesByWorkerId(workerId);
        return ResponseEntity.ok(analyses);
    }
}
