package com.example.demo.schedule.service;

import com.example.demo.schedule.model.Schedule;
import com.example.demo.schedule.repository.ScheduleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    // ── GET ALL (paginated) ───────────────────────────────────────────────
    public Page<Schedule> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.ASC, "scheduleId"));
        return scheduleRepository.findAll(pageable);
    }

    // ── GET ONE ───────────────────────────────────────────────────────────
    public Schedule getByScheduleId(String scheduleId) {
        return scheduleRepository.findByScheduleId(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Schedule not found: " + scheduleId));
    }

    // ── CREATE ────────────────────────────────────────────────────────────
    public Schedule create(Schedule schedule) {
        schedule.setScheduleId(generateNextId());
        return scheduleRepository.save(schedule);
    }

    // ── UPDATE ────────────────────────────────────────────────────────────
    public Schedule update(String scheduleId, Schedule incoming) {
        Schedule existing = getByScheduleId(scheduleId);
        existing.setDepartment(incoming.getDepartment());
        existing.setSemester(incoming.getSemester());
        existing.setSubjectId(incoming.getSubjectId());
        existing.setSubjectName(incoming.getSubjectName());
        existing.setFacultyId(incoming.getFacultyId());
        existing.setFacultyName(incoming.getFacultyName());
        existing.setVenueId(incoming.getVenueId());
        existing.setVenueName(incoming.getVenueName());
        existing.setDaySlots(incoming.getDaySlots());
        return scheduleRepository.save(existing);
    }

    // ── DELETE ────────────────────────────────────────────────────────────
    public void delete(String scheduleId) {
        scheduleRepository.delete(getByScheduleId(scheduleId));
    }

    // ── AUTO-ID GENERATOR (SCH-001, SCH-002 ...) ──────────────────────────
    private String generateNextId() {
        List<Schedule> all = scheduleRepository.findAll();
        int max = 0;
        for (Schedule s : all) {
            String sid = s.getScheduleId();
            if (sid != null && sid.startsWith("SCH-")) {
                try {
                    int n = Integer.parseInt(sid.substring(4));
                    if (n > max) max = n;
                } catch (NumberFormatException ignored) {}
            }
        }
        return String.format("SCH-%03d", max + 1);
    }
}
