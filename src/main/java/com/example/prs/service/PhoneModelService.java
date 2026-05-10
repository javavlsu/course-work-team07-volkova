package com.example.prs.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.prs.model.PhoneModel;
import com.example.prs.repository.PhoneModelRepository;

@Service
public class PhoneModelService {

    private final PhoneModelRepository repo;

    public PhoneModelService(PhoneModelRepository repo) {
        this.repo = repo;
    }

    public List<PhoneModel> getByBrandId(Long brandId) {
        return repo.findByPhoneBrandId(brandId);
    }

    public List<PhoneModel> search(String query) {
        return repo.findByNameContainingIgnoreCase(query);
    } 
}