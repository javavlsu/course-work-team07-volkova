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

    List<Review> findByClientId(Long clientId);

    @Query("select coalesce(avg(r.rating), 0) from Review r")
    Double getAverageRating();

    List<Review> findAllByOrderByCreatedAtDesc();

    List<Review> findAllByClientOrderByCreatedAtDesc(User client);
}