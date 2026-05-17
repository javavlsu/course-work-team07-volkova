package com.example.prs.controller;

import com.example.prs.model.User;
import com.example.prs.service.ReviewService;
import com.example.prs.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/client")
public class CreateReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    public CreateReviewController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @GetMapping("/createreview")
    public String createReviewPage() {
        return "client/createreview";
    }

    @PostMapping("/createreview")
    public String submitReview(@RequestParam int rating,
                            @RequestParam(required = false) String comment, 
                            RedirectAttributes redirectAttributes) {

        User currentUser = userService.getCurrentUser();

        reviewService.createReview(rating, comment, currentUser);

        redirectAttributes.addFlashAttribute("successMessage", "Спасибо за Ваш отзыв!");

        return "redirect:/client/myreviews";
    }
}