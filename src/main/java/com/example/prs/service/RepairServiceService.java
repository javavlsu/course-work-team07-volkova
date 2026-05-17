package com.example.prs.service;

import com.example.prs.model.RepairService;
import com.example.prs.repository.RepairServiceRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RepairServiceService {

    private final RepairServiceRepository repo;

    public RepairServiceService(RepairServiceRepository repo) {
        this.repo = repo;
    }

     public List<RepairService> getByModelId(Long modelId) {
         return repo.findByModelId(modelId);
    }

    public RepairService getById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public List<RepairService> getAll() {
        return repo.findAllWithDetails();
    }

     public List<RepairService> search(String query) {
        return repo.findByNameContainingIgnoreCase(query);
    }
}