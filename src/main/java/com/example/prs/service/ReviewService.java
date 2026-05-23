package com.example.prs.service;

import com.example.prs.model.Review;
import com.example.prs.model.User;
import com.example.prs.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ReviewService {

    private final ReviewRepository repo;

    public ReviewService(ReviewRepository repo) {
        this.repo = repo;
    }

    public Page<Review> getAllReviews(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public double getAverageRating() {
        return Math.round(repo.getAverageRating() * 10.0) / 10.0;
    }

    public void createReview(int rating, String comment, User client) {

        Review review = new Review();

        review.setRating(rating);
        review.setComment(comment);
        review.setClient(client);
        review.setCreatedAt(LocalDateTime.now());

        repo.save(review);
    }

    public Page<Review> getReviewsByUser(User client, Pageable pageable) {
        return repo.findAllByClient(client, pageable);
    }

    public void deleteReview(Long id) {
        repo.deleteById(id);
    }

    public List<Review> getLatestReviews() {
        return repo.findTop6ByOrderByCreatedAtDesc();
    }
}