package com.example.prs.controller;

import com.example.prs.model.Review;
import com.example.prs.service.ReviewService;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/reviews")
public class ReviewsController {

    private final ReviewService reviewService;

    public ReviewsController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public String reviewsPage( @RequestParam(required = false, defaultValue = "desc") String sortDir, Model model) {

        List<Review> reviews = reviewService.getAllReviews(); 
        // сортировка по дате создания
        if (reviews != null && !reviews.isEmpty()) {

            Comparator<Review> comparator = Comparator.comparing(
                    Review::getCreatedAt,
                    Comparator.nullsLast(Comparator.naturalOrder())
            );

            if ("asc".equalsIgnoreCase(sortDir)) {
                reviews.sort(comparator);
            } else {
                reviews.sort(comparator.reversed());
            }
        }

        double averageRating = reviewService.getAverageRating();

        model.addAttribute("reviews", reviews);
        model.addAttribute("currentDir", sortDir);
        model.addAttribute("averageRating", averageRating);

        return "reviews";
    }
}