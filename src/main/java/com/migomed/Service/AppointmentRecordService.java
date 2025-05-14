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

    // Получение всех записей
    public List<AppointmentRecord> getAllRecords() {
        return recordRepository.findAll();
    }

    // Создание новой записи
    public AppointmentRecord createRecord(AppointmentRecord record) {
        // Если в clientInfo можно распарсить число, предполагаем, что передан ID и пытаемся найти пользователя.
        try {
            Long clientId = Long.parseLong(record.getClientInfo());
            Optional<Users> clientOpt = usersRepository.findById(clientId);
            if (clientOpt.isPresent()) {
                Users client = clientOpt.get();
                // Опционально: заменить значение на ФИО клиента
                // String fio = client.getSurname() + " " + client.getName() + (client.getPatronymic() != null ? " " + client.getPatronymic() : "");
                // record.setClientInfo(fio);
                // Или оставить ID как строку:
                record.setClientInfo(String.valueOf(client.getId()));
            }
        } catch (NumberFormatException e) {
            // Если парсинг не удался, значит, передано ФИО – оставляем значение как есть.
        }
        return recordRepository.save(record);
    }

    // Обновление записи
    public AppointmentRecord updateRecord(Long id, AppointmentRecord updatedRecord) {
        AppointmentRecord existing = recordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Запись не найдена"));
        existing.setSpecialistId(updatedRecord.getSpecialistId());
        existing.setClientInfo(updatedRecord.getClientInfo());
        existing.setPhoneNumber(updatedRecord.getPhoneNumber());
        existing.setAppointmentDate(updatedRecord.getAppointmentDate());
        return recordRepository.save(existing);
    }

    // Удаление записи
    public void deleteRecord(Long id) {
        AppointmentRecord existing = recordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Запись не найдена"));
        recordRepository.delete(existing);
    }

    // Получение записи по id
    public Optional<AppointmentRecord> getRecordById(Long id) {
        return recordRepository.findById(id);
    }

    // Поиск записей по ФИО клиента (используем LIKE-поиск)
    public List<AppointmentRecord> searchByClientFio(String fio) {
        return recordRepository.findByClientInfoContainingIgnoreCase(fio);
    }

    // Поиск записей по ФИО специалиста:
    // Для каждой записи получаем специалиста через usersRepository (через workerId, если они связаны),
    // формируем полное ФИО и сравниваем с поисковым запросом.
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
