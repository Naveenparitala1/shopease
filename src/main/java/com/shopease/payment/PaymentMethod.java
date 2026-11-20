package com.shopease.payment;

public interface PaymentMethod {
    String getName();
    void pay(double amount);
}