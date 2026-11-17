package com.shopease.service;

import com.shopease.exception.AuthenticationException;
import com.shopease.exception.ShopException;
import com.shopease.model.Customer;
import com.shopease.repository.CustomerRepository;

public class AuthService {
    private final CustomerRepository customerRepository;

    public AuthService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer register(String name, String email, String password) {
        if (name == null || name.isBlank()) {
            throw new ShopException("Name cannot be empty");
        }
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new ShopException("Please enter a valid email address");
        }
        if (password == null || password.length() < 6) {
            throw new ShopException("Password must be at least 6 characters");
        }
        if (customerRepository.findByEmail(email.trim()).isPresent()) {
            throw new ShopException("An account with this email already exists");
        }

        int newId = customerRepository.count() + 1;
        Customer customer = new Customer(newId, name.trim(), email.trim().toLowerCase(), password);
        customerRepository.save(customer);
        return customer;
    }

    public Customer login(String email, String password) {
        Customer customer = customerRepository.findByEmail(email.trim())
                .orElseThrow(() -> new AuthenticationException("Invalid email or password"));

        if (!customer.getPassword().equals(password)) {
            throw new AuthenticationException("Invalid email or password");
        }
        return customer;
    }
}