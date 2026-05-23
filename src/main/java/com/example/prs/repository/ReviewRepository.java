package com.example.prs.repository;

import com.example.prs.model.Review;
import com.example.prs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByClientId(Long clientId);

    @Query("select coalesce(avg(r.rating), 0) from Review r")
    Double getAverageRating();

    Page<Review> findAll(Pageable pageable);
    Page<Review> findAllByClient(User client, Pageable pageable);

    List<Review> findTop6ByOrderByCreatedAtDesc();
}