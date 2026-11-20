package com.shopease.service;

import com.shopease.exception.InvalidQuantityException;
import com.shopease.exception.OutOfStockException;
import com.shopease.exception.ShopException;
import com.shopease.model.Cart;
import com.shopease.model.CartItem;
import com.shopease.model.Product;

public class CartService {
    private final ProductService productService;
    private final Cart cart = new Cart();

    public CartService(ProductService productService) {
        this.productService = productService;
    }

    public void addToCart(int productId, int quantity) {
        validateQuantity(quantity);
        Product product = productService.getProductById(productId);
        int alreadyInCart = cart.getItem(productId)
                .map(CartItem::getQuantity)
                .orElse(0);
        checkStock(product, alreadyInCart + quantity);
        cart.addItem(product, quantity);
    }

    public void updateQuantity(int productId, int quantity) {
        validateQuantity(quantity);
        Product product = productService.getProductById(productId);
        if (cart.getItem(productId).isEmpty()) {
            throw new ShopException("Product " + productId + " is not in your cart");
        }
        checkStock(product, quantity);
        cart.updateQuantity(productId, quantity);
    }

    public void removeFromCart(int productId) {
        if (cart.getItem(productId).isEmpty()) {
            throw new ShopException("Product " + productId + " is not in your cart");
        }
        cart.removeItem(productId);
    }

    public Cart getCart() {
        return cart;
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new InvalidQuantityException(quantity);
        }
    }

    private void checkStock(Product product, int requested) {
        if (requested > product.getStock()) {
            throw new OutOfStockException(product.getName(), product.getStock(), requested);
        }
    }
}