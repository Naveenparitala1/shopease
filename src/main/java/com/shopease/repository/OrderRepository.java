package com.shopease.repository;

import com.shopease.model.Order;

import java.util.List;

public interface OrderRepository {
    void save(Order order);
    List<Order> findByCustomerEmail(String email);
    int count();
}