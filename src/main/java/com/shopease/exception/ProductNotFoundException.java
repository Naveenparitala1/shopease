package com.shopease.exception;

public class ProductNotFoundException extends ShopException {
    public ProductNotFoundException(int productId) {
        super("Product not found with id: " + productId);
    }
}