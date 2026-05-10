package com.example.prs.listener;

import com.example.prs.event.OrderCompletedEvent;
import com.example.prs.model.Order;
import com.example.prs.model.Payment;
import com.example.prs.model.enums.PaymentMethod;
import com.example.prs.model.enums.PaymentStatus;
import com.example.prs.repository.OrderRepository;
import com.example.prs.repository.PaymentRepository;

import jakarta.transaction.Transactional;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

import java.time.LocalDateTime;

@Component
public class OrderCompletedListener {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public OrderCompletedListener(PaymentRepository paymentRepository,
                                  OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    @EventListener
    public void handle(OrderCompletedEvent event) {

        Long orderId = event.getOrder().getId();
        
        Order order = orderRepository.findById(orderId).orElseThrow();
        Payment payment = paymentRepository.findByOrderId(orderId).orElse(null);

        if (payment == null) {
            payment = new Payment();
            payment.setOrder(order);
            payment.setAmount(order.getPrice());
            payment.setMethod(PaymentMethod.OFFLINE);
        }

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaidAt(LocalDateTime.now());

        paymentRepository.save(payment);
    }
}