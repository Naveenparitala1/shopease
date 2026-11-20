package com.shopease.service;

import com.shopease.exception.OutOfStockException;
import com.shopease.exception.ShopException;
import com.shopease.model.Cart;
import com.shopease.model.CartItem;
import com.shopease.model.Customer;
import com.shopease.model.Order;
import com.shopease.model.OrderItem;
import com.shopease.model.Product;
import com.shopease.payment.PaymentMethod;
import com.shopease.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private final ProductService productService;
    private final OrderRepository orderRepository;

    public OrderService(ProductService productService, OrderRepository orderRepository) {
        this.productService = productService;
        this.orderRepository = orderRepository;
    }

    public Order placeOrder(Customer customer, Cart cart, PaymentMethod paymentMethod) {
        if (cart.isEmpty()) {
            throw new ShopException("Your cart is empty");
        }

        // 1. Re-check stock: it may have changed since the items were added
        for (CartItem item : cart.getItems()) {
            Product product = productService.getProductById(item.getProduct().getId());
            if (item.getQuantity() > product.getStock()) {
                throw new OutOfStockException(product.getName(), product.getStock(), item.getQuantity());
            }
        }

        // 2. Take payment before changing anything
        paymentMethod.pay(cart.getTotal());

        // 3. Reduce stock and copy each cart item into an order item
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            productService.reduceStock(product, item.getQuantity());
            orderItems.add(new OrderItem(product.getId(), product.getName(),
                    product.getPrice(), item.getQuantity()));
        }

        // 4. Save the order and empty the cart
        Order order = new Order(orderRepository.count() + 1, customer.getEmail(),
                orderItems, paymentMethod.getName());
        orderRepository.save(order);
        cart.clear();
        return order;
    }

    public List<Order> getOrderHistory(Customer customer) {
        return orderRepository.findByCustomerEmail(customer.getEmail());
    }
}