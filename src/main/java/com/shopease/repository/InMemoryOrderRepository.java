package com.shopease.repository;

import com.shopease.model.Order;

import java.util.ArrayList;
import java.util.List;

public class InMemoryOrderRepository implements OrderRepository {
    private final List<Order> orders = new ArrayList<>();

    @Override
    public void save(Order order) {
        orders.add(order);
    }

    @Override
    public List<Order> findByCustomerEmail(String email) {
        return orders.stream()
                .filter(order -> order.getCustomerEmail().equalsIgnoreCase(email))
                .toList();
    }

    @Override
    public int count() {
        return orders.size();
    }
}