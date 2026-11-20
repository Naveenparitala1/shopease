package com.shopease.ui;

import com.shopease.exception.ShopException;
import com.shopease.model.Cart;
import com.shopease.model.CartItem;
import com.shopease.model.Customer;
import com.shopease.model.Order;
import com.shopease.model.Product;
import com.shopease.payment.CardPayment;
import com.shopease.payment.PaymentMethod;
import com.shopease.payment.WalletPayment;
import com.shopease.service.AuthService;
import com.shopease.service.CartService;
import com.shopease.service.OrderService;
import com.shopease.service.ProductService;

import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {
    private static final double DEMO_WALLET_BALANCE = 500.00;

    private final Scanner scanner = new Scanner(System.in);
    private final ProductService productService;
    private final AuthService authService;
    private final CartService cartService;
    private final OrderService orderService;
    private Customer currentCustomer;

    public ConsoleMenu(ProductService productService, AuthService authService,
                       CartService cartService, OrderService orderService) {
        this.productService = productService;
        this.authService = authService;
        this.cartService = cartService;
        this.orderService = orderService;
    }

    public void start() {
        System.out.println("=== Welcome to ShopEase ===");
        boolean running = true;
        while (running) {
            try {
                if (currentCustomer == null) {
                    running = showGuestMenu();
                } else {
                    showCustomerMenu();
                }
            } catch (ShopException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println("Thank you for visiting ShopEase. Goodbye!");
    }

    private boolean showGuestMenu() {
        System.out.println();
        System.out.println("1. View products   2. Register   3. Login   0. Exit");
        int choice = readInt("Choose an option: ");
        switch (choice) {
            case 1 -> printProducts(productService.getAllProducts());
            case 2 -> register();
            case 3 -> login();
            case 0 -> {
                return false;
            }
            default -> System.out.println("Invalid option, please try again.");
        }
        return true;
    }

    private void showCustomerMenu() {
        System.out.println();
        System.out.println("--- Logged in as " + currentCustomer.getName() + " ---");
        System.out.println(" 1. View all products     2. Search by name      3. Browse by category");
        System.out.println(" 4. Filter by max price   5. Sort by price       6. Add to cart");
        System.out.println(" 7. View cart             8. Update quantity     9. Remove from cart");
        System.out.println("10. Checkout             11. Order history       0. Logout");
        int choice = readInt("Choose an option: ");
        switch (choice) {
            case 1 -> printProducts(productService.getAllProducts());
            case 2 -> printProducts(productService.searchByName(readText("Search for: ")));
            case 3 -> browseByCategory();
            case 4 -> printProducts(productService.filterByMaxPrice(readDouble("Maximum price: ")));
            case 5 -> printProducts(productService.sortByPrice(
                    readText("Lowest price first? (y/n): ").equalsIgnoreCase("y")));
            case 6 -> addToCart();
            case 7 -> printCart();
            case 8 -> updateQuantity();
            case 9 -> removeFromCart();
            case 10 -> checkout();
            case 11 -> printOrderHistory();
            case 0 -> logout();
            default -> System.out.println("Invalid option, please try again.");
        }
    }

    private void register() {
        String name = readText("Name: ");
        String email = readText("Email: ");
        String password = readText("Password (min 6 characters): ");
        Customer customer = authService.register(name, email, password);
        System.out.println("Account created for " + customer.getName() + ". You can log in now.");
    }

    private void login() {
        String email = readText("Email: ");
        String password = readText("Password: ");
        currentCustomer = authService.login(email, password);
        System.out.println("Welcome back, " + currentCustomer.getName() + "!");
    }

    private void logout() {
        cartService.getCart().clear();
        System.out.println("Goodbye, " + currentCustomer.getName() + ". You are logged out.");
        currentCustomer = null;
    }

    private void browseByCategory() {
        System.out.println("Categories: " + String.join(", ", productService.getCategories()));
        printProducts(productService.getByCategory(readText("Category: ")));
    }

    private void addToCart() {
        int productId = readInt("Product id: ");
        int quantity = readInt("Quantity: ");
        cartService.addToCart(productId, quantity);
        System.out.println("Added to cart.");
    }

    private void updateQuantity() {
        int productId = readInt("Product id: ");
        int quantity = readInt("New quantity: ");
        cartService.updateQuantity(productId, quantity);
        System.out.println("Cart updated.");
    }

    private void removeFromCart() {
        cartService.removeFromCart(readInt("Product id: "));
        System.out.println("Removed from cart.");
    }

    private void checkout() {
        Cart cart = cartService.getCart();
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }
        printCart();
        System.out.println("Pay with: 1. Card   2. ShopEase Wallet (balance " + DEMO_WALLET_BALANCE + ")");
        PaymentMethod paymentMethod = switch (readInt("Choose payment: ")) {
            case 1 -> new CardPayment(readText("Card number (16 digits): "));
            case 2 -> new WalletPayment(DEMO_WALLET_BALANCE);
            default -> throw new ShopException("Invalid payment option");
        };
        Order order = orderService.placeOrder(currentCustomer, cart, paymentMethod);
        System.out.println("Order placed successfully!");
        System.out.println(order);
    }

    private void printProducts(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }
        System.out.printf("%-4s %-20s %-12s %10s %6s%n", "ID", "Name", "Category", "Price", "Stock");
        products.forEach(System.out::println);
    }

    private void printCart() {
        Cart cart = cartService.getCart();
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }
        for (CartItem item : cart.getItems()) {
            System.out.println(item);
        }
        System.out.printf("Cart total: %.2f%n", cart.getTotal());
    }

    private void printOrderHistory() {
        List<Order> orders = orderService.getOrderHistory(currentCustomer);
        if (orders.isEmpty()) {
            System.out.println("You have no orders yet.");
            return;
        }
        orders.forEach(order -> System.out.println(order + "\n"));
    }

    private String readText(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int readInt(String prompt) {
        while (true) {
            String input = readText(prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            String input = readText(prompt);
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number, for example 99.99");
            }
        }
    }
}