package com.example.prs.event;

import com.example.prs.model.Order;

public class OrderCompletedEvent {

    private final Order order;

    public OrderCompletedEvent(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}