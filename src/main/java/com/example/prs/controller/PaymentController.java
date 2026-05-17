package com.example.prs.controller;

import com.example.prs.model.Order;
import com.example.prs.service.OrderService;
import com.example.prs.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final OrderService orderService;
    private final PaymentService paymentService;

    public PaymentController(OrderService orderService,
                             PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @GetMapping("/payorder/{orderId}")
    public String paymentPage(@PathVariable Long orderId, Model model) {

        Order order = orderService.getById(orderId);

        model.addAttribute("order", order);
        model.addAttribute("user", order.getClient());

        return "payment/payorder";
    }

    @PostMapping("/offline/{orderId}")
    public String offline(@PathVariable Long orderId,
                          RedirectAttributes ra) {

        paymentService.payOffline(orderId);

        ra.addFlashAttribute("successMessage", "Вы выбрали оффлайн оплату. Оплатить можно будет на кассе в сервисном центре при выдаче устройства.");

        return "redirect:/client/myorders";
    }

    @PostMapping("/payorder/{orderId}")
    public String payOrder(@PathVariable Long orderId,
                        @RequestParam String cardNumber,
                        RedirectAttributes ra) {

        cardNumber = cardNumber.replaceAll("\\s+", "");

        if (!cardNumber.matches("\\d{16}")) {
            ra.addFlashAttribute("errorMessage", "Карта должна содержать 16 цифр");
            return "redirect:/payment/payorder/" + orderId;
        }

        paymentService.payOnline(orderId);

        ra.addFlashAttribute("successMessage", "Оплата прошла успешно!");

        return "redirect:/client/myorders";
    }
}