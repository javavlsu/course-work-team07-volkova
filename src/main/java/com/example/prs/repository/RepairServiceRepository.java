package com.example.prs.repository;

import com.example.prs.model.RepairService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RepairServiceRepository extends JpaRepository<RepairService, Long> {

    @Query("select rs from RepairService rs where rs.phoneModel.id = :modelId")
    List<RepairService> findByModelId(@Param("modelId") Long modelId);

    List<RepairService> findByName(String name);

    @Query("select rs from RepairService rs join fetch rs.phoneModel pm join fetch pm.phoneBrand")
    List<RepairService> findAllWithDetails();

    List<RepairService> findByNameContainingIgnoreCase(String name);
}