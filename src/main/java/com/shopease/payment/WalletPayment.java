package com.shopease.payment;

import com.shopease.exception.PaymentFailedException;

public class WalletPayment implements PaymentMethod {
    private double balance;

    public WalletPayment(double balance) {
        this.balance = balance;
    }

    @Override
    public String getName() {
        return "ShopEase Wallet";
    }

    @Override
    public void pay(double amount) {
        if (amount > balance) {
            throw new PaymentFailedException(String.format(
                    "Wallet balance %.2f is less than the order total %.2f", balance, amount));
        }
        balance -= amount;
        System.out.printf("Paid %.2f from wallet. Remaining balance: %.2f%n", amount, balance);
    }
}