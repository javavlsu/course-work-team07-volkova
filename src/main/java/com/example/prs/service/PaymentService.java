package com.example.prs.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.prs.model.Order;
import com.example.prs.model.Payment;
import com.example.prs.model.enums.PaymentMethod;
import com.example.prs.model.enums.PaymentStatus;
import com.example.prs.repository.PaymentRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;

    public PaymentService(PaymentRepository paymentRepository, OrderService orderService){
        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
    }

    public void payOffline(Long orderId) {

        Order order = orderService.getById(orderId);

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getPrice());
        payment.setStatus(PaymentStatus.WAITING);
        payment.setMethod(PaymentMethod.OFFLINE);

        paymentRepository.save(payment);
    }

    public void payOnline(Long orderId) {

        Order order = orderService.getById(orderId);

        Payment payment = paymentRepository.findByOrderId(orderId).orElse(null);

        if (payment == null) {
            payment = new Payment();
            payment.setOrder(order);
            payment.setAmount(order.getPrice());
        }

        payment.setMethod(PaymentMethod.ONLINE);
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaidAt(LocalDateTime.now());

        paymentRepository.save(payment);
    }

    public void ensureCompletedPayment(Order order) {

        if (order.getPayment() != null) return;

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getPrice());
        payment.setMethod(PaymentMethod.OFFLINE);
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaidAt(LocalDateTime.now());

        paymentRepository.save(payment);
    }
    
}
