package com.migomed.Service;

import com.migomed.Entity.Analysis;
import com.migomed.Entity.Users;
import com.migomed.Entity.Worker;
import com.migomed.Repository.AnalysisRepository;
import com.migomed.Repository.UsersRepository;
import com.migomed.Repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnalysisService {

    private final AnalysisRepository analysisRepository;
    private final WorkerRepository workerRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public AnalysisService(AnalysisRepository analysisRepository,
                           WorkerRepository workerRepository,
                           UsersRepository usersRepository) {
        this.analysisRepository = analysisRepository;
        this.workerRepository = workerRepository;
        this.usersRepository = usersRepository;
    }

    public Analysis createAnalysis(Long workerId, Long userId, Analysis analysis) {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new IllegalArgumentException("Работник не найден с id " + workerId));
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден с id " + userId));
        analysis.setWorker(worker);
        analysis.setUser(user);
        return analysisRepository.save(analysis);
    }


    public Analysis updateAnalysis(Long id, Analysis updatedAnalysis) {
        Analysis analysis = analysisRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Анализ не найден с id " + id));
        analysis.setAnalysisDate(updatedAnalysis.getAnalysisDate());
        analysis.setResult(updatedAnalysis.getResult());
        return analysisRepository.save(analysis);
    }


    public void deleteAnalysis(Long id) {
        if (!analysisRepository.existsById(id)) {
            throw new IllegalArgumentException("Анализ не найден с id " + id);
        }
        analysisRepository.deleteById(id);
    }

    public Optional<Analysis> getAnalysisById(Long id) {
        return analysisRepository.findById(id);
    }


    public List<Analysis> getAllAnalyses() {
        return analysisRepository.findAll();
    }


    public List<Analysis> getAnalysesByUserId(Long userId) {
        return analysisRepository.findByUser_Id(userId);
    }


    public List<Analysis> getAnalysesByWorkerId(Long workerId) {
        return analysisRepository.findByWorker_Id(workerId);
    }
}
