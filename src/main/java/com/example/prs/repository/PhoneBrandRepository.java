package com.example.prs.repository;

import com.example.prs.model.PhoneBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для управления марками телефонов
 */
@Repository
public interface PhoneBrandRepository extends JpaRepository<PhoneBrand, Long> {

    /**
     * Поиск марки телефона по названию
     * @param name название марки
     * @return Optional с найденной маркой
     */
    Optional<PhoneBrand> findByName(String name);

    /**
     * Проверка существования марки с указанным названием
     * @param name название марки
     * @return true - марка уже существует, иначе false
     */
    boolean existsByName(String name);

    List<PhoneBrand> findByNameContainingIgnoreCase(String name);
}