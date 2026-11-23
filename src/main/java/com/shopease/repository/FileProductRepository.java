package com.shopease.repository;

import com.shopease.model.Product;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FileProductRepository implements ProductRepository {
    private final Path filePath;
    private final Map<Integer, Product> products = new LinkedHashMap<>();

    public FileProductRepository(String fileName) {
        this.filePath = Path.of(fileName);
        if (Files.exists(filePath)) {
            loadFromFile();
        } else {
            new InMemoryProductRepository().findAll()
                    .forEach(product -> products.put(product.getId(), product));
            saveToFile();
        }
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
        saveToFile();
    }

    private void loadFromFile() {
        try {
            for (String line : Files.readAllLines(filePath)) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split(",");
                Product product = new Product(
                        Integer.parseInt(parts[0]),
                        parts[1],
                        parts[2],
                        Double.parseDouble(parts[3]),
                        Integer.parseInt(parts[4]));
                products.put(product.getId(), product);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read " + filePath, e);
        }
    }

    private void saveToFile() {
        List<String> lines = products.values().stream()
                .map(p -> p.getId() + "," + p.getName() + "," + p.getCategory()
                        + "," + p.getPrice() + "," + p.getStock())
                .toList();
        try {
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not write " + filePath, e);
        }
    }
}