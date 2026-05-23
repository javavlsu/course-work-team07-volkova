package com.example.prs.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.prs.model.PhoneModel;
import com.example.prs.repository.PhoneModelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class PhoneModelService {

    private final PhoneModelRepository repo;

    public PhoneModelService(PhoneModelRepository repo) {
        this.repo = repo;
    }

    public List<PhoneModel> getByBrandId(Long brandId) {
        return repo.findByPhoneBrandId(brandId);
    }

    public Page<PhoneModel> search(String query, Pageable pageable) {
        return repo.findByNameContainingIgnoreCase(query, pageable);
    }
}