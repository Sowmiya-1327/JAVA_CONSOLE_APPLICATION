package com.aspiresys.grocerymanagement;

public class GroceryItem {
    private int id;
    private String name;
    private String category;
    private double price;
    private int quantity; // New field for quantity
    private double discount; // New field for discount

    public GroceryItem(int id, String name, String category, double price, int quantity, double discount) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity; // Assign quantity
        this.discount = discount; // Assign discount
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price - (price * discount / 100); // Apply discount
    }

    public int getQuantity() {
        return quantity;
    }

    public double getDiscount() {
        return discount;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Category: " + category +
               ", Price: $" + price + ", Quantity: " + quantity + ", Discount: " + discount + "%";
    }
}
