package com.shopease.model;

public class Product {
    private final int id;
    private final String name;
    private final String category;
    private final double price;
    private int stock;

    public Product(int id, String name, String category, double price, int stock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }

    public void reduceStock(int quantity) {
        this.stock -= quantity;
    }

    @Override
    public String toString() {
        return String.format("%-4d %-20s %-12s %10.2f %6d", id, name, category, price, stock);
    }
}