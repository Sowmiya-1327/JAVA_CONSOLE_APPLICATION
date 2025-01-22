package com.aspiresys.grocerymanagement;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class GroceryManagement {
    private Database database;
    private Scanner scanner = new Scanner(System.in);
    private Cart cart = new Cart();
    private User currentUser;
    private boolean isAdmin = false;

    public GroceryManagement(Database database) {
        this.database = database;
    }

    public void start() {
        while (true) {
            System.out.println("1. Login\n2. Register\n3. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    System.exit(0);
            }
        }
    }

    private void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (!Validation.isValidUsername(username) || !Validation.isValidPassword(password)) {
            System.out.println("Invalid username or password format.");
            return;
        }

        try {
            currentUser = database.loginUser(username, password);
            if (currentUser != null) {
                System.out.println("Login successful! Welcome, " + currentUser.getUsername());
                if (currentUser.getRole().equals("admin")) {
                    isAdmin = true;
                    adminMenu();
                } else {
                    userMenu();
                }
            } else {
                System.out.println("Invalid username or password.");
            }
        } catch (SQLException sQLException) {
            System.out.println("Error during login: " + sQLException.getMessage());
        }
    }

    private void register() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        String role = "user"; // Default role

        if (!Validation.isValidUsername(username) || !Validation.isValidPassword(password)) {
            System.out.println("Invalid username or password format.");
            return;
        }

        User newUser = new User(username, password, role);
        try {
            database.registerUser(newUser);
            System.out.println("Registration successful!");
        } catch (SQLException sQLException) {
            System.out.println("Error during registration: " + sQLException.getMessage());
        }
    }

    private void userMenu() {
        while (!isAdmin) {
            System.out.println("User Menu:");
            System.out.println("1. View Grocery Items\n2. Search Items\n3. Add to Cart\n4. View Cart\n5. Update Cart\n6. Checkout\n7. Logout");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewItems();
                    break;
                case 2:
                    searchItems();
                    break;
                case 3:
                    addToCart();
                    break;
                case 4:
                    cart.viewCart();
                    break;
                case 5:
                    updateCart();
                    break;
                case 6:
                    cart.checkout();
                    break;
                case 7:
                    currentUser = null;
                    return; // Logout
            }
        }
    }

    private void adminMenu() {
        while (isAdmin) {
            System.out.println("Admin Menu:");
            System.out.println("1. Add Item\n2. Update Item\n3. Remove Item\n4. View Items\n5. Logout");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addItem();
                    break;
                case 2:
                    updateItem();
                    break;
                case 3:
                    removeItem();
                    break;
                case 4:
                    viewItems();
                    break;
                case 5:
                    currentUser = null;
                    isAdmin = false;
                    return; // Logout
            }
        }
    }

    private void viewItems() {
        try {
            List<GroceryItem> items = database.getAllItems();
            System.out.println("Available items:");
            for (GroceryItem item : items) {
                System.out.println(item);
            }
        } catch (SQLException sQLException) {
            System.out.println("Error fetching items: " + sQLException.getMessage());
        }
    }

    private void searchItems() {
        System.out.print("Enter search term: ");
        String searchTerm = scanner.nextLine();
        try {
            List<GroceryItem> items = database.searchItems(searchTerm);
            if (items.isEmpty()) {
                System.out.println("No items found.");
            } else {
                System.out.println("Search results:");
                for (GroceryItem item : items) {
                    System.out.println(item);
                }
            }
        } catch (SQLException sQLException) {
            System.out.println("Error searching items: " + sQLException.getMessage());
        }
    }

    private void addToCart() {
        System.out.print("Enter item ID to add to cart: ");
        int itemId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try {
            List<GroceryItem> items = database.getAllItems();
            for (GroceryItem item : items) {
                if (item.getId() == itemId) {
                    cart.addItem(item);
                    System.out.println("Added to cart: " + item.getName());
                    return;
                }
            }
            System.out.println("Item not found.");
        } catch (SQLException sQLException) {
            System.out.println("Error adding item to cart: " + sQLException.getMessage());
        }
    }

    private void updateCart() {
        cart.viewCart();
        System.out.print("Enter index of item to update: ");
        int index = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter new item ID: ");
        int newItemId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try {
            List<GroceryItem> items = database.getAllItems();
            for (GroceryItem item : items) {
                if (item.getId() == newItemId) {
                    cart.updateItem(index, item);
                    System.out.println("Updated cart with: " + item.getName());
                    return;
                }
            }
            System.out.println("New item not found.");
        } catch (SQLException sQLException) {
            System.out.println("Error updating cart: " + sQLException.getMessage());
        }
    }

    private void addItem() {
        System.out.print("Enter item name: ");
        String name = scanner.nextLine();
        System.out.print("Enter item category: ");
        String category = scanner.nextLine();
        System.out.print("Enter item price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter item quantity: ");
        int quantity = scanner.nextInt();
        System.out.print("Enter item discount (%): ");
        double discount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        GroceryItem item = new GroceryItem(0, name, category, price, quantity, discount); // ID will be generated by DB
        try {
            database.addItem(item);
            System.out.println("Item added: " + name);
        } catch (SQLException sQLException) {
            System.out.println("Error adding item: " + sQLException.getMessage());
        }
    }

    private void updateItem() {
        System.out.print("Enter item ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try {
            List<GroceryItem> items = database.getAllItems();
            for (GroceryItem item : items) {
                if (item.getId() == id) {
                    System.out.print("Enter new item name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter new item category: ");
                    String category = scanner.nextLine();
                    System.out.print("Enter new item price: ");
                    double price = scanner.nextDouble();
                    System.out.print("Enter new item quantity: ");
                    int quantity = scanner.nextInt();
                    System.out.print("Enter new item discount (%): ");
                    double discount = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline

                    GroceryItem updatedItem = new GroceryItem(id, name, category, price, quantity, discount);
                    database.updateItem(updatedItem);
                    System.out.println("Item updated: " + name);
                    return;
                }
            }
            System.out.println("Item not found.");
        } catch (SQLException sQLException) {
            System.out.println("Error updating item: " + sQLException.getMessage());
        }
    }

    private void removeItem() {
        System.out.print("Enter item ID to remove: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try {
            database.removeItem(id);
            System.out.println("Item removed with ID: " + id);
        } catch (SQLException sQLException) {
            System.out.println("Error removing item: " + sQLException.getMessage());
        }
    }
}
