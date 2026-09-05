package com.example.demo.hostel.service;

import com.example.demo.hostel.model.HostelBlock;
import com.example.demo.hostel.repository.HostelBlockRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HostelBlockService {

    private final HostelBlockRepository repo;

    public HostelBlockService(HostelBlockRepository repo) {
        this.repo = repo;
    }

    public Page<HostelBlock> getAll(int page, int size) {
        return repo.findAll(PageRequest.of(page, size));
    }

    public List<HostelBlock> getAll() {
        return repo.findAll();
    }

    public HostelBlock getByBlockId(String blockId) {
        // Try by blockId field first, then fall back to MongoDB _id
        return repo.findByBlockId(blockId)
                .or(() -> repo.findById(blockId))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Hostel block not found: " + blockId));
    }

    public HostelBlock create(HostelBlock block) {
        if (block.getHostelName() == null || block.getHostelName().isBlank())
            throw new IllegalArgumentException("Hostel name is required.");
        if (block.getType() == null || block.getType().isBlank())
            throw new IllegalArgumentException("Type (BOYS/GIRLS) is required.");

        // Find the highest existing blockId number and increment from there
        // This prevents collision when records are deleted and re-created
        String nextId = generateNextBlockId();
        block.setBlockId(nextId);
        block.setActive(true);
        return repo.save(block);
    }

    private String generateNextBlockId() {
        // Get all blockIds, parse the number part, find max, add 1
        int max = repo.findAll().stream()
                .map(HostelBlock::getBlockId)
                .filter(id -> id != null && id.startsWith("HB"))
                .mapToInt(id -> {
                    try { return Integer.parseInt(id.substring(2)); }
                    catch (NumberFormatException e) { return 0; }
                })
                .max()
                .orElse(0);
        return String.format("HB%03d", max + 1);
    }

    public HostelBlock update(String blockId, HostelBlock incoming) {
        HostelBlock existing = getByBlockId(blockId);
        existing.setHostelName(incoming.getHostelName());
        existing.setType(incoming.getType());
        existing.setActive(incoming.isActive());
        return repo.save(existing);
    }

    public void delete(String blockId) {
        repo.delete(getByBlockId(blockId));
    }
}
