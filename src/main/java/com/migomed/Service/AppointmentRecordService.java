package com.migomed.Service;

import com.migomed.Entity.AppointmentRecord;
import com.migomed.Entity.Users;
import com.migomed.Repository.AppointmentRecordRepository;
import com.migomed.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentRecordService {

    private final AppointmentRecordRepository recordRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public AppointmentRecordService(AppointmentRecordRepository recordRepository, UsersRepository usersRepository) {
        this.recordRepository = recordRepository;
        this.usersRepository = usersRepository;
    }

    public List<AppointmentRecord> getAllRecords() {
        return recordRepository.findAll();
    }

    public AppointmentRecord createRecord(AppointmentRecord record) {
        try {
            Long clientId = Long.parseLong(record.getClientInfo());
            Optional<Users> clientOpt = usersRepository.findById(clientId);
            if (clientOpt.isPresent()) {
                Users client = clientOpt.get();
                record.setClientInfo(String.valueOf(client.getId()));
            }
        } catch (NumberFormatException e) {
        }
        return recordRepository.save(record);
    }

    public AppointmentRecord updateRecord(Long id, AppointmentRecord updatedRecord) {
        AppointmentRecord existing = recordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Запись не найдена"));
        existing.setSpecialistId(updatedRecord.getSpecialistId());
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

    public List<AppointmentRecord> searchByClientFio(String fio) {
        return recordRepository.findByClientInfoContainingIgnoreCase(fio);
    }

    public List<AppointmentRecord> searchBySpecialistFio(String fio) {
        List<AppointmentRecord> allRecords = recordRepository.findAll();
        return allRecords.stream().filter(record -> {
            Optional<Users> specialistOpt = usersRepository.findById(record.getSpecialistId());
            if (specialistOpt.isPresent()) {
                Users specialist = specialistOpt.get();
                String fullName = specialist.getSurname() + " " + specialist.getName() +
                        (specialist.getPatronymic() != null ? " " + specialist.getPatronymic() : "");
                return fullName.toLowerCase().contains(fio.toLowerCase());
            }
            return false;
        }).collect(Collectors.toList());
    }
}
