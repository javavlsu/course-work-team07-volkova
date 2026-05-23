package com.example.prs.controller;

import com.example.prs.model.Order;
import com.example.prs.model.User;
import com.example.prs.model.enums.OrderStatus;
import com.example.prs.service.OrderService;
import com.example.prs.service.UserService;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    private final OrderService orderService;
    private final UserService userService;

    public EmployeeController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        Map<OrderStatus, Long> count = orderService.countByOrderStatus();

        model.addAttribute("totalOrders", count.values().stream().mapToLong(Long::longValue).sum());
        model.addAttribute("createdOrders", count.getOrDefault(OrderStatus.CREATED, 0L));
        model.addAttribute("inRepairOrders", count.getOrDefault(OrderStatus.INREPAIR, 0L));
        model.addAttribute("readyOrders", count.getOrDefault(OrderStatus.READY, 0L));
        model.addAttribute("completedOrders", count.getOrDefault(OrderStatus.COMPLETED, 0L));
        model.addAttribute("canceledOrders", count.getOrDefault(OrderStatus.CANCELED, 0L));

        return "employee/dashboard";
    }

    @GetMapping("/orders")
    public String orders(
        @RequestParam(required = false) Boolean onlyMine, 
        @RequestParam(defaultValue = "ALL") String status,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "desc") String sortDir,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        Model model) {

        User currentUser = userService.getCurrentUser();

        Sort sort = sortDir.equals("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        OrderStatus orderStatus = null;

        if (!"ALL".equals(status)) {

            try {
                orderStatus = OrderStatus.valueOf(status);
            } catch (Exception ignored) {}
        }

        Page<Order> ordersPage = orderService.getOrdersForEmployee(currentUser.getId(), onlyMine, orderStatus, pageable);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("orders", ordersPage.getContent());
        model.addAttribute("ordersPage", ordersPage);
        model.addAttribute("onlyMine", onlyMine);
        model.addAttribute("currentStatus", status);
        model.addAttribute("currentSort", sortBy);
        model.addAttribute("currentDir", sortDir);
        model.addAttribute("statuses", OrderStatus.values());

        return "employee/orders";
    }

    @PostMapping("/orders/{id}/status")
    public String updateStatus(@PathVariable Long id,
                            @RequestParam OrderStatus status,
                            RedirectAttributes ra) {

        User employee = userService.getCurrentUser();

        try {
            orderService.changeStatus(id, status, employee);

            ra.addFlashAttribute("successMessage", "Статус успешно обновлён!");

        } catch (IllegalStateException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/employee/orders";
    }

        @PostMapping("/orders/{orderId}/price")
        public String changePrice(@PathVariable Long orderId,
                                @RequestParam BigDecimal price,
                                RedirectAttributes ra) {
            try {
                orderService.changePrice(orderId, price, userService.getCurrentUser());
                ra.addFlashAttribute("successMessage", "Цена успешно обновлена!");
            } catch (Exception e) {
                ra.addFlashAttribute("errorMessage", e.getMessage());
            }

            return "redirect:/employee/orders";
        }
}