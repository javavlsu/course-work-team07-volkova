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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Controller
@RequestMapping("/reviews")
public class ReviewsController {

    private final ReviewService reviewService;

    public ReviewsController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public String reviewsPage(
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            Model model) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by("createdAt").ascending()
                : Sort.by("createdAt").descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Review> reviewsPage = reviewService.getAllReviews(pageable);

        double averageRating = reviewService.getAverageRating();

        model.addAttribute("reviews", reviewsPage.getContent());
        model.addAttribute("reviewsPage", reviewsPage);
        model.addAttribute("currentDir", sortDir);
        model.addAttribute("averageRating", averageRating);

        return "reviews";
    }
}