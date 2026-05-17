package com.example.prs.repository;

import com.example.prs.model.PhoneBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PhoneBrandRepository extends JpaRepository<PhoneBrand, Long> {

    Optional<PhoneBrand> findByName(String name);

    List<PhoneBrand> findByNameContainingIgnoreCase(String name);
}