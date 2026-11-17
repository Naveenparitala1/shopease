package com.shopease.service;

import com.shopease.exception.ProductNotFoundException;
import com.shopease.model.Product;
import com.shopease.repository.ProductRepository;

import java.util.Comparator;
import java.util.List;

public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(int id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public List<Product> searchByName(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return productRepository.findAll().stream()
                .filter(product -> product.getName().toLowerCase().contains(lowerKeyword))
                .toList();
    }

    public List<Product> filterByMaxPrice(double maxPrice) {
        return productRepository.findAll().stream()
                .filter(product -> product.getPrice() <= maxPrice)
                .toList();
    }

    public List<Product> sortByPrice(boolean ascending) {
        Comparator<Product> byPrice = Comparator.comparingDouble(Product::getPrice);
        return productRepository.findAll().stream()
                .sorted(ascending ? byPrice : byPrice.reversed())
                .toList();
    }

    public List<String> getCategories() {
        return productRepository.findAll().stream()
                .map(Product::getCategory)
                .distinct()
                .sorted()
                .toList();
    }

    public List<Product> getByCategory(String category) {
        return productRepository.findAll().stream()
                .filter(product -> product.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    public void reduceStock(Product product, int quantity) {
        product.reduceStock(quantity);
        productRepository.save(product);
    }
}