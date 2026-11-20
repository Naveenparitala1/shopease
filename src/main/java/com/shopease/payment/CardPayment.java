package com.shopease.payment;

import com.shopease.exception.PaymentFailedException;

public class CardPayment implements PaymentMethod {
    private final String cardNumber;

    public CardPayment(String cardNumber) {
        if (cardNumber == null || !cardNumber.matches("\\d{16}")) {
            throw new PaymentFailedException("Card number must be exactly 16 digits");
        }
        this.cardNumber = cardNumber;
    }

    @Override
    public String getName() {
        return "Card ending in " + cardNumber.substring(12);
    }

    @Override
    public void pay(double amount) {
        System.out.printf("Charged %.2f to %s%n", amount, getName());
    }
}