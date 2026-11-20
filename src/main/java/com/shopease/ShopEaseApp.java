package com.shopease;

import com.shopease.repository.CustomerRepository;
import com.shopease.repository.InMemoryCustomerRepository;
import com.shopease.repository.InMemoryOrderRepository;
import com.shopease.repository.InMemoryProductRepository;
import com.shopease.repository.OrderRepository;
import com.shopease.repository.ProductRepository;
import com.shopease.service.AuthService;
import com.shopease.service.CartService;
import com.shopease.service.OrderService;
import com.shopease.service.ProductService;
import com.shopease.ui.ConsoleMenu;

public class ShopEaseApp {
    public static void main(String[] args) {
        // 1. Repositories: where data lives
        ProductRepository productRepository = new InMemoryProductRepository();
        CustomerRepository customerRepository = new InMemoryCustomerRepository();
        OrderRepository orderRepository = new InMemoryOrderRepository();

        // 2. Services: business rules, each given what it needs
        ProductService productService = new ProductService(productRepository);
        AuthService authService = new AuthService(customerRepository);
        CartService cartService = new CartService(productService);
        OrderService orderService = new OrderService(productService, orderRepository);

        // 3. User interface
        new ConsoleMenu(productService, authService, cartService, orderService).start();
    }
}