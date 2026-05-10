package com.example.prs.controller;

import com.example.prs.model.Order;
import com.example.prs.model.RepairService;
import com.example.prs.model.User;
import com.example.prs.service.OrderService;
import com.example.prs.service.RepairServiceService;
import com.example.prs.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/client")
public class OrderController {

    private final OrderService orderService;
    private final RepairServiceService repairServiceService;
    private final UserService userService;

    public OrderController(OrderService orderService,
                           RepairServiceService repairServiceService,
                           UserService userService) {
        this.orderService = orderService;
        this.repairServiceService = repairServiceService;
        this.userService = userService;
    }

    @GetMapping("/createorder")
    public String createOrderPage(@RequestParam Long serviceId, Model model) {
        var service = repairServiceService.getById(serviceId);

        model.addAttribute("service", service);
        model.addAttribute("model", service.getPhoneModel());
        model.addAttribute("brand", service.getPhoneModel().getPhoneBrand());

        User currentUser = userService.getCurrentUser();
        model.addAttribute("user", currentUser);

        return "client/createorder";
    }

    @PostMapping("/createorder")
    public String createOrder(@RequestParam Long serviceId,
                             @RequestParam(required = false) String description,
                             @RequestParam String contactEmail,
                             @RequestParam String contactNumber,
                             RedirectAttributes redirectAttributes) {
        
        User currentUser = userService.getCurrentUser();

        orderService.createOrder(serviceId, description, contactEmail, contactNumber, currentUser);

        redirectAttributes.addFlashAttribute("successMessage", "Заказ успешно оформлен! Наш сотрудник свяжется с Вами по указанным контактным данным в ближайшее время.");

        return "redirect:/client/myorders";
    }
}