package com.shopease.exception;

public class OutOfStockException extends ShopException {
    public OutOfStockException(String productName, int available, int requested) {
        super("Not enough stock for " + productName
                + ". Available: " + available + ", requested: " + requested);
    }
}