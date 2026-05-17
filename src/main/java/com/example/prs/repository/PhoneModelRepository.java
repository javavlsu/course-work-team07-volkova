package com.example.prs.repository;

import com.example.prs.model.PhoneModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PhoneModelRepository extends JpaRepository<PhoneModel, Long> {

    List<PhoneModel> findByPhoneBrandId(Long phoneBrandId);

    Optional<PhoneModel> findByName(String name);

    List<PhoneModel> findByNameContainingIgnoreCase(String name);
}