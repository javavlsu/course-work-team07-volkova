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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
        @RequestParam(defaultValue = "ALL")String status, 
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "desc") String sortDir,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        Model model) {

        Sort sort = sortDir.equals("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        OrderStatus orderStatus = null;

        if (!"ALL".equals(status)) {

            try {
                orderStatus = OrderStatus.valueOf(status);
            } catch (IllegalArgumentException e) {
                orderStatus = null;
            }
        }

        Page<Order> ordersPage = orderService.getOrdersForClient(userService.getCurrentUser(), orderStatus, pageable);

        model.addAttribute("orders", ordersPage.getContent());
        model.addAttribute("ordersPage", ordersPage);
        model.addAttribute("currentStatus", status);
        model.addAttribute("currentSort", sortBy);
        model.addAttribute("currentDir", sortDir);
        model.addAttribute("statuses", OrderStatus.values());

        return "/client/myorders";
    }

}