package com.migomed.Service;

import com.migomed.Entity.Visit;
import com.migomed.Entity.Users;
import com.migomed.Entity.Worker;
import com.migomed.Repository.VisitRepository;
import com.migomed.Repository.UsersRepository;
import com.migomed.Repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final WorkerRepository workerRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public VisitService(VisitRepository visitRepository,
                        WorkerRepository workerRepository,
                        UsersRepository usersRepository) {
        this.visitRepository = visitRepository;
        this.workerRepository = workerRepository;
        this.usersRepository = usersRepository;
    }

    public Visit createVisit(Long workerId, Long userId, Visit visit) {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new IllegalArgumentException("Сотрудник не найден с id " + workerId));
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден с id " + userId));
        visit.setWorker(worker);
        visit.setUser(user);
        return visitRepository.save(visit);
    }

    public Visit updateVisit(Long id, Visit updatedVisit) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Визит не найден с id " + id));
        visit.setDateVisit(updatedVisit.getDateVisit());
        visit.setAppointments(updatedVisit.getAppointments());
        return visitRepository.save(visit);
    }

    public void deleteVisit(Long id) {
        if (!visitRepository.existsById(id)) {
            throw new IllegalArgumentException("Визит не найден с id " + id);
        }
        visitRepository.deleteById(id);
    }

    public Optional<Visit> getVisitById(Long id) {
        return visitRepository.findById(id);
    }

    public List<Visit> getAllVisits() {
        return visitRepository.findAll();
    }

    public List<Visit> getVisitsByUserId(Long userId) {
        return visitRepository.findByUser_Id(userId);
    }

    public List<Visit> getVisitsByWorkerId(Long workerId) {
        return visitRepository.findByWorker_Id(workerId);
    }
}
