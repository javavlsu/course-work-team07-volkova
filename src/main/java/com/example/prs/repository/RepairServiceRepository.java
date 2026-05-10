package com.example.prs.repository;

import com.example.prs.model.RepairService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

/**
 * Репозиторий для управления ремонтными услугами
 */
@Repository
public interface RepairServiceRepository extends JpaRepository<RepairService, Long> {

    /**
     * Поиск всеч услуг, доступных для конкретной модели телефона
     * @param modelId id модели телефона
     * @return список услуг для данной модели
     */
    @Query("select rs from RepairService rs where rs.phoneModel.id = :modelId")
    List<RepairService> findByModelId(@Param("modelId") Long modelId);

    /**
     * Поиск услуг по названию (частичное совпадение)
     * @param name часть названия услуги
     * @return список подходящих услуг
     */
    List<RepairService> findByName(String name);

    /**
     * Поиск услуг в указанном ценовом диапазоне
     * @param minPrice минимальная цена
     * @param maxPrice максимальная цена
     * @return список услуг в ценовом диапазоне
     */
    List<RepairService> findByEstimatedPrice(BigDecimal minPrice, BigDecimal maxPrice);

    @Query("select rs from RepairService rs join fetch rs.phoneModel pm join fetch pm.phoneBrand")
    List<RepairService> findAllWithDetails();

    List<RepairService> findByNameContainingIgnoreCase(String name);
}