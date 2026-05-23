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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
    public String myReviews(
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            Model model) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by("createdAt").ascending()
                : Sort.by("createdAt").descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Review> reviewsPage = reviewService.getReviewsByUser(userService.getCurrentUser(), pageable);

        model.addAttribute("reviews", reviewsPage.getContent());
        model.addAttribute("reviewsPage", reviewsPage);
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
