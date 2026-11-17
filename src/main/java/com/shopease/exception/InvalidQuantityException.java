package com.shopease.exception;

public class InvalidQuantityException extends ShopException {
    public InvalidQuantityException(int quantity) {
        super("Quantity must be greater than 0, but was: " + quantity);
    }
}