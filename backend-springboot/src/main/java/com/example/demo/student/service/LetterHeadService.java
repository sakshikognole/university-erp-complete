package com.example.demo.student.service;

import com.example.demo.student.model.LetterHead;
import com.example.demo.student.repository.LetterHeadRepository;
import org.springframework.stereotype.Service;

@Service
public class LetterHeadService {

    private final LetterHeadRepository letterHeadRepository;

    public LetterHeadService(LetterHeadRepository letterHeadRepository) {
        this.letterHeadRepository = letterHeadRepository;
    }

    /** Returns the single letterhead document, or a blank default if not yet seeded. */
    public LetterHead get() {
        return letterHeadRepository.findByKey("default")
                .orElseGet(LetterHead::new);
    }

    /** Upsert — always saves as key="default". */
    public LetterHead save(LetterHead incoming) {
        LetterHead existing = letterHeadRepository.findByKey("default")
                .orElseGet(LetterHead::new);
        existing.setKey("default");
        existing.setTrustName(incoming.getTrustName());
        existing.setCollegeName(incoming.getCollegeName());
        existing.setAddress(incoming.getAddress());
        existing.setPhone(incoming.getPhone());
        existing.setTollFree(incoming.getTollFree());
        existing.setFax(incoming.getFax());
        existing.setWebsite(incoming.getWebsite());
        existing.setEmail(incoming.getEmail());
        existing.setLogoText(incoming.getLogoText());
        return letterHeadRepository.save(existing);
    }
}
