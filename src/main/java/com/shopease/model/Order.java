package com.shopease.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Order {
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final int id;
    private final String customerEmail;
    private final List<OrderItem> items;
    private final double total;
    private final String paymentMethod;
    private final LocalDateTime orderDate;

    public Order(int id, String customerEmail, List<OrderItem> items, String paymentMethod) {
        this.id = id;
        this.customerEmail = customerEmail;
        this.items = List.copyOf(items);
        this.total = items.stream().mapToDouble(OrderItem::subtotal).sum();
        this.paymentMethod = paymentMethod;
        this.orderDate = LocalDateTime.now();
    }

    public int getId() { return id; }
    public String getCustomerEmail() { return customerEmail; }
    public List<OrderItem> getItems() { return items; }
    public double getTotal() { return total; }
    public String getPaymentMethod() { return paymentMethod; }
    public LocalDateTime getOrderDate() { return orderDate; }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        text.append(String.format("Order #%d | %s | Paid with: %s%n",
                id, orderDate.format(DATE_FORMAT), paymentMethod));
        for (OrderItem item : items) {
            text.append(String.format("   %-20s %3d x %8.2f = %10.2f%n",
                    item.productName(), item.quantity(), item.unitPrice(), item.subtotal()));
        }
        text.append(String.format("   Total: %.2f", total));
        return text.toString();
    }
}