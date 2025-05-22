package com.migomed.Service;

import com.migomed.Entity.AppointmentRecord;
import com.migomed.Entity.Worker;
import com.migomed.Repository.AppointmentRecordRepository;
import com.migomed.Repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentRecordService {

    private final AppointmentRecordRepository recordRepository;
    private final WorkerRepository workerRepository;

    @Autowired
    public AppointmentRecordService(AppointmentRecordRepository recordRepository, WorkerRepository workerRepository) {
        this.recordRepository = recordRepository;
        this.workerRepository = workerRepository;
    }

    public List<AppointmentRecord> getAllRecords() {
        return recordRepository.findAll();
    }

    public AppointmentRecord createRecord(AppointmentRecord record) {
        if (record.getWorker() != null && record.getWorker().getId() != null) {
            Optional<Worker> workerOpt = workerRepository.findById(record.getWorker().getId());
            if (workerOpt.isPresent()) {
                record.setWorker(workerOpt.get());
            } else {
                throw new IllegalArgumentException("Сотрудник не найден");
            }
        }
        return recordRepository.save(record);
    }

    public AppointmentRecord updateRecord(Long id, AppointmentRecord updatedRecord) {
        AppointmentRecord existing = recordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Запись не найдена"));
        if (updatedRecord.getWorker() != null && updatedRecord.getWorker().getId() != null) {
            Worker worker = workerRepository.findById(updatedRecord.getWorker().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Сотрудник не найден"));
            existing.setWorker(worker);
        }
        existing.setClientInfo(updatedRecord.getClientInfo());
        existing.setPhoneNumber(updatedRecord.getPhoneNumber());
        existing.setAppointmentDate(updatedRecord.getAppointmentDate());
        return recordRepository.save(existing);
    }

    public void deleteRecord(Long id) {
        AppointmentRecord existing = recordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Запись не найдена"));
        recordRepository.delete(existing);
    }

    public Optional<AppointmentRecord> getRecordById(Long id) {
        return recordRepository.findById(id);
    }

    public List<AppointmentRecord> findRecordsByWorkerId(Long workerId) {
        return recordRepository.findByWorker_Id(workerId);
    }
}
