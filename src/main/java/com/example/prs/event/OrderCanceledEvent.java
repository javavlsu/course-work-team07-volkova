package com.example.prs.event;

import com.example.prs.model.Order;

public class OrderCanceledEvent {

    private final Order order;

    public OrderCanceledEvent(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}