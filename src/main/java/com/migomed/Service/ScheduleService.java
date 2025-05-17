package com.migomed.Service;

import com.migomed.Entity.Schedule;
import com.migomed.Entity.Worker;
import com.migomed.Repository.ScheduleRepository;
import com.migomed.Repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final WorkerRepository workerRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository, WorkerRepository workerRepository) {
        this.scheduleRepository = scheduleRepository;
        this.workerRepository = workerRepository;
    }

    public Schedule createSchedule(Schedule schedule) {
        Long workerId = schedule.getWorker().getId();
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new IllegalArgumentException("Сотрудник не найден с ID " + workerId));
        schedule.setWorker(worker);
        return scheduleRepository.save(schedule);
    }

    public Schedule updateSchedule(Long id, Schedule updatedSchedule) {
        Schedule existing = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Расписание не найдено с ID " + id));

        existing.setWorkDay(updatedSchedule.getWorkDay());
        existing.setStartTime(updatedSchedule.getStartTime());
        existing.setEndTime(updatedSchedule.getEndTime());

        if (updatedSchedule.getWorker() != null && updatedSchedule.getWorker().getId() != null) {
            Worker worker = workerRepository.findById(updatedSchedule.getWorker().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Сотрудник не найден с ID " + updatedSchedule.getWorker().getId()));
            existing.setWorker(worker);
        }

        return scheduleRepository.save(existing);
    }

    public void deleteSchedule(Long id) {
        Schedule existing = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Расписание не найдено с ID " + id));
        scheduleRepository.delete(existing);
    }

    public Optional<Schedule> getScheduleById(Long id) {
        return scheduleRepository.findById(id);
    }

    public List<Schedule> getSchedulesByWorkerId(Long workerId) {
        return scheduleRepository.findByWorker_Id(workerId);
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }
}
