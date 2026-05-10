package com.example.prs.service;

import com.example.prs.model.Review;
import com.example.prs.model.User;
import com.example.prs.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository repo;

    public ReviewService(ReviewRepository repo) {
        this.repo = repo;
    }

    // все отзывы
    public List<Review> getAllReviews() {
        return repo.findAllByOrderByCreatedAtDesc();
    }

    // создание отзыва
    public void createReview(int rating, String comment, User client) {

        Review review = new Review();

        review.setRating(rating);
        review.setComment(comment);
        review.setClient(client);
        review.setCreatedAt(LocalDateTime.now());

        repo.save(review);
    }

    // мои отзывы
    public List<Review> getReviewsByUser(User client) {
        return repo.findAllByClientOrderByCreatedAtDesc(client);
    }

    // удаление отзыва
    public void deleteReview(Long id) {
        repo.deleteById(id);
    }
}