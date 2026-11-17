package com.shopease.repository;

import com.shopease.model.Customer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryCustomerRepository implements CustomerRepository {
    private final Map<String, Customer> customersByEmail = new HashMap<>();

    @Override
    public Optional<Customer> findByEmail(String email) {
        return Optional.ofNullable(customersByEmail.get(email.toLowerCase()));
    }

    @Override
    public void save(Customer customer) {
        customersByEmail.put(customer.getEmail().toLowerCase(), customer);
    }

    @Override
    public int count() {
        return customersByEmail.size();
    }
}