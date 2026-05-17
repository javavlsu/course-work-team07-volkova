package com.example.prs.controller;

import com.example.prs.service.PhoneBrandService;
import com.example.prs.service.PhoneModelService;
import com.example.prs.service.RepairServiceService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/catalog")
public class CatalogController {

    private final PhoneBrandService phoneBrandService;
    private final PhoneModelService phoneModelService;
    private final RepairServiceService repairServiceService;

    public CatalogController(PhoneBrandService phoneBrandService,
                             PhoneModelService phoneModelService,
                             RepairServiceService repairServiceService) {
        this.phoneBrandService = phoneBrandService;
        this.phoneModelService = phoneModelService;
        this.repairServiceService = repairServiceService;
    }

    @GetMapping
    public String catalog(@RequestParam(required = false) Long brandId,
                          @RequestParam(required = false) Long modelId,
                          @RequestParam(required = false) String search,
                          Model model) {

        model.addAttribute("search", search);
        if (search != null && !search.isBlank()) {
            model.addAttribute("brands", phoneBrandService.search(search));
            model.addAttribute("models", phoneModelService.search(search));

            return "catalog";
        }

        model.addAttribute("brands", phoneBrandService.getAll());
        if (brandId != null) {
            model.addAttribute("selectedBrandId", brandId);
            model.addAttribute("models", phoneModelService.getByBrandId(brandId));
        }
        if (modelId != null) {
            model.addAttribute("selectedModelId", modelId);
            model.addAttribute("services", repairServiceService.getByModelId(modelId));
        }
        
        return "catalog";
    }
}