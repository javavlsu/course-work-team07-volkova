package com.example.prs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.prs.service.PhoneBrandService;
import com.example.prs.service.PhoneModelService;
import com.example.prs.service.RepairServiceService;
import com.example.prs.service.ReviewService;

@Controller
public class HomeController {

    private final PhoneBrandService phoneBrandService;
    private final ReviewService reviewService;

    public HomeController(PhoneBrandService phoneBrandService, ReviewService reviewService) {
        this.phoneBrandService = phoneBrandService;
        this.reviewService = reviewService;
    }

    @GetMapping("/")
    public String index(Model model) {

        model.addAttribute("brands", phoneBrandService.getAll().stream().limit(6).toList());
        model.addAttribute("reviews", reviewService.getAllReviews().stream().limit(6).toList());

        return "index";
    }
    
    @GetMapping("/contacts")
    public String contacts() {
        return "contacts";
    }
}
