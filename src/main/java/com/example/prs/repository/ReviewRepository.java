package com.example.prs.repository;

import com.example.prs.model.Review;
import com.example.prs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Репозиторий для управления отзывами клиентов
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Поиск всех конкретного клиента
     * @param clientId id клиента
     * @return список отзывов клиента
     */
    List<Review> findByClientId(Long clientId);

    /**
     * Подсчет среднего рейтинга всех отзывов
     * @return средняя оценка (или 0, если отзывов нет)
     */
    @Query("select coalese(avg(r.rating), 0) from Review r")
    Double getAverageRating();


    List<Review> findAllByOrderByCreatedAtDesc();
    List<Review> findAllByClientOrderByCreatedAtDesc(User client);
}