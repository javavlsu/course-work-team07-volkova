package com.example.prs.controller;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.prs.model.Review;
import com.example.prs.service.ReviewService;
import com.example.prs.service.UserService;

@Controller
@RequestMapping("/client")
public class MyReviewsController {
    
    private final ReviewService reviewService;
    private final UserService userService;

    public MyReviewsController(ReviewService reviewService,
                                  UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @GetMapping("/myreviews")
    public String myReviews(@RequestParam(required = false, defaultValue = "desc") String sortDir, Model model) {

        List<Review> reviews = reviewService.getReviewsByUser(userService.getCurrentUser());

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

        model.addAttribute("reviews", reviews);
        model.addAttribute("currentDir", sortDir);

        return "client/myreviews";
    }

    @PostMapping("/reviews/delete/{id}")
    public String deleteReview(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        reviewService.deleteReview(id);

        redirectAttributes.addFlashAttribute("successMessage", "Отзыв успешно удален!");

        return "redirect:/client/myreviews";
    }
    
}
