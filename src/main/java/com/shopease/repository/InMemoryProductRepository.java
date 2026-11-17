package com.shopease.repository;

import com.shopease.model.Product;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryProductRepository implements ProductRepository {
    private final Map<Integer, Product> products = new LinkedHashMap<>();

    public InMemoryProductRepository() {
        save(new Product(1, "Laptop", "Electronics", 899.99, 10));
        save(new Product(2, "Headphones", "Electronics", 59.99, 25));
        save(new Product(3, "Keyboard", "Accessories", 39.99, 0));
        save(new Product(4, "Office Chair", "Furniture", 149.50, 5));
        save(new Product(5, "Java Book", "Books", 45.00, 12));
        save(new Product(6, "Mouse", "Accessories", 19.99, 40));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    @Override
    public Optional<Product> findById(int id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public void save(Product product) {
        products.put(product.getId(), product);
    }
}