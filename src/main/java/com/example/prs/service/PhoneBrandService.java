package com.example.prs.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.prs.model.PhoneBrand;
import com.example.prs.repository.PhoneBrandRepository;

@Service
public class PhoneBrandService {

    private final PhoneBrandRepository repo;

    public PhoneBrandService(PhoneBrandRepository repo) {
        this.repo = repo;
    }

    public List<PhoneBrand> getAll() {
        return repo.findAll();
    }

    public List<PhoneBrand> search(String query) {
        return repo.findByNameContainingIgnoreCase(query);
    }
}