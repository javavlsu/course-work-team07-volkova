package com.example.prs.controller;

import com.example.prs.model.Order;
import com.example.prs.model.enums.OrderStatus;
import com.example.prs.service.OrderService;
import com.example.prs.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MyOrdersController {

    private final OrderService orderService;
    private final UserService userService;

    public MyOrdersController(OrderService orderService, 
                                   UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/client/myorders")
    public String clientMyOrders(
            @RequestParam(required = false) String status,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortDir,
            Model model) {
        
        List<Order> orders = orderService.getOrdersForClient(userService.getCurrentUser());
        
        // фильтр по статусу
        if (status != null && !status.isEmpty() && !"ALL".equals(status)) {
            try {
                OrderStatus orderStatus = OrderStatus.valueOf(status);
                orders = orders.stream()
                        .filter(order -> order.getStatus() == orderStatus)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {}
        }
        
        // сортировка
        if (orders != null && !orders.isEmpty()) {
            Comparator<Order> comparator;
            
            if ("repairedAt".equals(sortBy)) {
                comparator = Comparator.comparing(
                    order -> order.getRepairedAt() != null ? order.getRepairedAt() : order.getCreatedAt(),
                    Comparator.nullsLast(Comparator.naturalOrder())
                );
            } else if ("price".equals(sortBy)) {
                comparator = Comparator.comparing(Order::getPrice);
            } else {
                comparator = Comparator.comparing(Order::getCreatedAt);
            }
            
            if ("asc".equals(sortDir)) {
                orders.sort(comparator);
            } else {
                orders.sort(comparator.reversed());
            }
        }
        
        model.addAttribute("orders", orders);
        model.addAttribute("currentStatus", status != null ? status : "ALL");
        model.addAttribute("currentSort", sortBy);
        model.addAttribute("currentDir", sortDir);
        model.addAttribute("statuses", OrderStatus.values());
        
        return "/client/myorders";
    }
}