package com.example.prs.controller;

import com.example.prs.model.PhoneModel;
import com.example.prs.service.PhoneBrandService;
import com.example.prs.service.PhoneModelService;
import com.example.prs.service.RepairServiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "18") int size,
                        Model model) {

        model.addAttribute("search", search);

        if (search != null && !search.isBlank()) {

            Pageable pageable = PageRequest.of(page, size);

            Page<PhoneModel> modelsPage = phoneModelService.search(search, pageable);

            model.addAttribute("brands", phoneBrandService.search(search));
            model.addAttribute("models", modelsPage.getContent());
            model.addAttribute("modelsPage", modelsPage);
            model.addAttribute("services",  repairServiceService.search(search));

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