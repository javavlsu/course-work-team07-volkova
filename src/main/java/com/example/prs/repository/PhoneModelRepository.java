package com.example.prs.repository;

import com.example.prs.model.PhoneModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для управления моделями телефонов
 */
@Repository
public interface PhoneModelRepository extends JpaRepository<PhoneModel, Long> {

    /**
     * Поиск всех моделей конкретной марки
     * @param phoneBrandId id марки телефона
     * @return список моделей данной марки
     */
    List<PhoneModel> findByPhoneBrandId(Long phoneBrandId);

    /**
     * Поиск модели по названию
     * @param name название модели
     * @return Optional с найденной моделью
     */
    Optional<PhoneModel> findByName(String name);

    List<PhoneModel> findByNameContainingIgnoreCase(String name);
}