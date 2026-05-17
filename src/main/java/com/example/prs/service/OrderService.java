package com.example.prs.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.prs.model.Order;
import com.example.prs.model.RepairService;
import com.example.prs.model.User;
import com.example.prs.model.enums.OrderStatus;
import com.example.prs.repository.OrderRepository;

import org.springframework.context.ApplicationEventPublisher;

import com.example.prs.event.OrderCanceledEvent;
import com.example.prs.event.OrderCompletedEvent;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RepairServiceService repairServiceService;

    private final ApplicationEventPublisher eventPublisher;

    private static final Map<OrderStatus, Set<OrderStatus>> allowedTransitions = Map.of(
        OrderStatus.CREATED, Set.of(OrderStatus.INREPAIR, OrderStatus.CANCELED),

        OrderStatus.INREPAIR, Set.of(OrderStatus.READY, OrderStatus.CANCELED),

        OrderStatus.READY, Set.of(OrderStatus.COMPLETED),

        OrderStatus.COMPLETED, Set.of(),

        OrderStatus.CANCELED, Set.of()
    );

    public OrderService(OrderRepository orderRepository,
                        RepairServiceService repairServiceService, 
                        ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.repairServiceService = repairServiceService;
        this.eventPublisher=eventPublisher;
    }

    public void createOrder(Long serviceId, String description,
                            String contactEmail, String contactNumber, User user) {
        RepairService service = repairServiceService.getById(serviceId);
        Order order = new Order();
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.CREATED);
        order.setRepairService(service);
        order.setPhoneModel(service.getPhoneModel());
        order.setPhoneBrand(service.getPhoneModel().getPhoneBrand());
        order.setPrice(service.getEstimatedPrice());
        order.setDescription(description);
        order.setContactEmail(contactEmail);
        order.setContactNumber(contactNumber);
        order.setClient(user);
        orderRepository.save(order);
    }
    
    @Transactional(readOnly = true)
    public List<Order> getOrdersForClient(User client) {
        return orderRepository.findAllByClientOrderByCreatedAtDesc(client);
    }

    public long countAllOrders() {
        return orderRepository.count();
    }

    public Map<OrderStatus, Long> countByOrderStatus() {

        return orderRepository.countOrdersByStatus()
                .stream()
                .collect(Collectors.toMap(
                        r -> (OrderStatus) r[0],
                        r -> (Long) r[1]
                ));
    }

    @Transactional
    public void changeStatus(Long orderId, OrderStatus newStatus, User employee) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));

        OrderStatus current = order.getStatus();

        if (current == OrderStatus.COMPLETED || current == OrderStatus.CANCELED) {
            throw new IllegalStateException("Заказ уже закрыт");
        }

        if (newStatus.ordinal() < current.ordinal()) {
            throw new IllegalStateException("Нельзя откатить статус назад");
        }

        if (order.getEmployee() != null &&
            !order.getEmployee().getId().equals(employee.getId())) {
            throw new IllegalStateException("Заказ закреплён за другим сотрудником");
        }

        if (order.getEmployee() == null) {
            order.setEmployee(employee);
        }

        if (newStatus == OrderStatus.COMPLETED) {
            order.setRepairedAt(LocalDateTime.now());
        }

        order.setStatus(newStatus);

        orderRepository.save(order);

        // события после сохранения
        if (newStatus == OrderStatus.COMPLETED) {
            eventPublisher.publishEvent(new OrderCompletedEvent(order));
        }

        if (newStatus == OrderStatus.CANCELED) {
            eventPublisher.publishEvent(new OrderCanceledEvent(order));
        }
    }

    @Transactional
    public void changePrice(Long orderId, BigDecimal newPrice, User employee) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));

        if (order.getStatus() == OrderStatus.COMPLETED ||
            order.getStatus() == OrderStatus.CANCELED) {
            throw new IllegalStateException("Нельзя менять цену закрытого заказа");
        }

        if (newPrice == null || newPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Цена должна быть больше 0");
        }

        if (order.getEmployee() != null && !order.getEmployee().getId().equals(employee.getId())) {
            throw new IllegalStateException("Заказ закреплён за другим сотрудником");
        }

        if (order.getEmployee() == null) {
            order.setEmployee(employee);
        }

        order.setPrice(newPrice);

        orderRepository.save(order);
    }


    public List<Order> findByEmployee(Long employeeId) {
        return orderRepository.findByEmployeeId(employeeId);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order getById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Заказ не найден: " + id));
    }
}