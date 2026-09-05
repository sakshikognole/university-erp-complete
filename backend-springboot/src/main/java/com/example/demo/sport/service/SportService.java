package com.example.demo.sport.service;

import com.example.demo.sport.model.Sport;
import com.example.demo.sport.repository.SportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SportService {

    private final SportRepository sportRepository;

    public SportService(SportRepository sportRepository) {
        this.sportRepository = sportRepository;
    }

    public List<Sport> getAll() {
        return sportRepository.findAll();
    }

    public Sport getBySportId(String sportId) {
        return sportRepository.findBySportId(sportId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Sport not found with ID: " + sportId));
    }

    public Sport create(Sport sport) {
        if (sportRepository.existsBySportId(sport.getSportId().trim())) {
            throw new IllegalArgumentException(
                    "Sport ID '" + sport.getSportId() + "' already exists.");
        }
        sport.setSportId(sport.getSportId().trim());
        return sportRepository.save(sport);
    }

    public Sport update(String sportId, Sport incoming) {
        Sport existing = getBySportId(sportId);
        existing.setSportName(incoming.getSportName());
        existing.setCapacity(incoming.getCapacity());
        existing.setStatus(incoming.getStatus());
        existing.setDescription(incoming.getDescription());
        existing.setVenueId(incoming.getVenueId());
        return sportRepository.save(existing);
    }

    public void delete(String sportId) {
        sportRepository.delete(getBySportId(sportId));
    }
}
