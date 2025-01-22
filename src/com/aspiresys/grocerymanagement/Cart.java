package com.aspiresys.grocerymanagement;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<GroceryItem> items = new ArrayList<>();

    public void addItem(GroceryItem item) {
        items.add(item);
    }

    public void viewCart() {
        if (items.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        System.out.println("Items in cart:");
        for (GroceryItem item : items) {
            System.out.println(item);
        }
    }

    public void updateItem(int index, GroceryItem newItem) {
        if (index >= 0 && index < items.size()) {
            items.set(index, newItem);
        } else {
            System.out.println("Invalid index.");
        }
    }

    public void checkout() {
        if (items.isEmpty()) {
            System.out.println("Cart is empty. Cannot checkout.");
            return;
        }
        double total = 0;
        for (GroceryItem item : items) {
            total += item.getPrice() * item.getQuantity(); // Multiply by quantity
        }
        System.out.println("Checkout successful! Total amount: $" + total);
        items.clear(); // Clear the cart after checkout
    }

    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
        } else {
            System.out.println("Invalid index.");
        }
    }

    public List<GroceryItem> getItems() {
        return items;
    }
}
