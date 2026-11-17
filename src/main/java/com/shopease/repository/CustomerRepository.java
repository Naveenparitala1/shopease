package com.shopease.repository;

import com.shopease.model.Customer;

import java.util.Optional;

public interface CustomerRepository {
    Optional<Customer> findByEmail(String email);
    void save(Customer customer);
    int count();
}