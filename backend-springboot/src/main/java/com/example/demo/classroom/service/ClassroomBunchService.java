package com.example.demo.classroom.service;

import com.example.demo.classroom.model.Classroom;
import com.example.demo.classroom.model.ClassroomBunch;
import com.example.demo.classroom.repository.ClassroomBunchRepository;
import com.example.demo.classroom.repository.ClassroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassroomBunchService {

    @Autowired
    private ClassroomBunchRepository bunchRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    // Generate next bunch ID
    private String generateBunchId() {
        List<ClassroomBunch> all = bunchRepository.findAll();
        int max = all.stream()
                .map(ClassroomBunch::getBunchId)
                .filter(id -> id != null && id.startsWith("BUNCH-"))
                .map(id -> id.substring(6))
                .mapToInt(num -> {
                    try { return Integer.parseInt(num); }
                    catch (NumberFormatException e) { return 0; }
                })
                .max()
                .orElse(0);
        return String.format("BUNCH-%03d", max + 1);
    }

    // Create bunch
    public ClassroomBunch createBunch(ClassroomBunch bunch) {
        bunch.setBunchId(generateBunchId());
        
        // Calculate totals
        bunch.setTotalClassrooms(bunch.getClassroomIds().size());
        
        final int[] total = {0};
        bunch.getClassroomIds().forEach(classroomId -> {
            classroomRepository.findByClassroomId(classroomId)
                    .ifPresent(classroom -> total[0] += classroom.getCapacity());
        });
        bunch.setTotalCapacity(total[0]);
        
        return bunchRepository.save(bunch);
    }

    // Get all bunches
    public List<ClassroomBunch> getAllBunches() {
        return bunchRepository.findAll();
    }

    // Get bunch by ID
    public ClassroomBunch getBunchById(String bunchId) {
        return bunchRepository.findByBunchId(bunchId);
    }

    // Update bunch
    public ClassroomBunch updateBunch(String bunchId, ClassroomBunch updates) {
        ClassroomBunch existing = bunchRepository.findByBunchId(bunchId);
        if (existing == null) {
            throw new RuntimeException("Bunch not found: " + bunchId);
        }
        
        existing.setBunchName(updates.getBunchName());
        existing.setClassroomIds(updates.getClassroomIds());
        
        // Recalculate totals
        existing.setTotalClassrooms(updates.getClassroomIds().size());
        
        final int[] total = {0};
        updates.getClassroomIds().forEach(classroomId -> {
            classroomRepository.findByClassroomId(classroomId)
                    .ifPresent(classroom -> total[0] += classroom.getCapacity());
        });
        existing.setTotalCapacity(total[0]);
        
        return bunchRepository.save(existing);
    }

    // Delete bunch
    public void deleteBunch(String bunchId) {
        ClassroomBunch bunch = bunchRepository.findByBunchId(bunchId);
        if (bunch == null) {
            throw new RuntimeException("Bunch not found: " + bunchId);
        }
        bunchRepository.delete(bunch);
    }
}
