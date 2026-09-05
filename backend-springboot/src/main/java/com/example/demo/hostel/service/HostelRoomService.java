package com.example.demo.hostel.service;

import com.example.demo.hostel.model.HostelRoom;
import com.example.demo.hostel.model.RoomStudent;
import com.example.demo.hostel.repository.HostelRoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HostelRoomService {

    private final HostelRoomRepository repo;

    public HostelRoomService(HostelRoomRepository repo) {
        this.repo = repo;
    }

    // ── Queries ──────────────────────────────────────────────────────────

    public List<HostelRoom> getByBlock(String blockId) {
        return repo.findByBlockId(blockId);
    }

    public HostelRoom getByRoomId(String roomId) {
        // Try roomId field first, then fall back to MongoDB _id
        return repo.findByRoomId(roomId)
                .or(() -> repo.findById(roomId))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Room not found: " + roomId));
    }

    // ── Create ───────────────────────────────────────────────────────────

    public HostelRoom create(HostelRoom room) {
        if (room.getBlockId() == null || room.getBlockId().isBlank())
            throw new IllegalArgumentException("Block is required.");
        if (room.getRoomNo() == null || room.getRoomNo().isBlank())
            throw new IllegalArgumentException("Room number is required.");
        if (room.getCapacity() <= 0)
            throw new IllegalArgumentException("Capacity must be greater than 0.");
        if (repo.existsByBlockIdAndRoomNo(room.getBlockId(), room.getRoomNo().trim()))
            throw new IllegalArgumentException(
                    "Room " + room.getRoomNo() + " already exists in this block.");

        // Use max existing roomId number to avoid duplicates on delete/recreate
        room.setRoomId(generateNextRoomId());
        room.setRoomNo(room.getRoomNo().trim());
        return repo.save(room);
    }

    private String generateNextRoomId() {
        int max = repo.findAll().stream()
                .map(HostelRoom::getRoomId)
                .filter(id -> id != null && id.startsWith("HR"))
                .mapToInt(id -> {
                    try {
                        // Handle both HR001 and HR-XXXXXX formats
                        String num = id.substring(2).replaceAll("[^0-9]", "");
                        return num.isEmpty() ? 0 : Integer.parseInt(num);
                    } catch (NumberFormatException e) { return 0; }
                })
                .max()
                .orElse(0);
        return String.format("HR%03d", max + 1);
    }

    // ── Delete ───────────────────────────────────────────────────────────

    public void delete(String roomId) {
        repo.delete(getByRoomId(roomId));
    }

    // ── Student roster ───────────────────────────────────────────────────

    public HostelRoom addStudent(String roomId, String prn, String name) {
        HostelRoom room = getByRoomId(roomId);

        if (room.getStudents().size() >= room.getCapacity())
            throw new IllegalArgumentException(
                    "Room " + room.getRoomNo() + " is at full capacity.");

        boolean duplicate = room.getStudents().stream()
                .anyMatch(s -> s.getStudentPrn().equals(prn));
        if (duplicate)
            throw new IllegalArgumentException(
                    "Student with PRN " + prn + " is already in this room.");

        room.getStudents().add(new RoomStudent(prn, name));
        return repo.save(room);
    }

    public HostelRoom removeStudent(String roomId, String prn) {
        HostelRoom room = getByRoomId(roomId);
        boolean removed = room.getStudents()
                .removeIf(s -> s.getStudentPrn().equals(prn));
        if (!removed)
            throw new IllegalArgumentException("Student not found in this room.");
        return repo.save(room);
    }
}
