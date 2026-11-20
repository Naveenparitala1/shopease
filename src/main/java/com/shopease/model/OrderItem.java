package com.shopease.model;

public record OrderItem(int productId, String productName, double unitPrice, int quantity) {
    public double subtotal() {
        return unitPrice * quantity;
    }
}