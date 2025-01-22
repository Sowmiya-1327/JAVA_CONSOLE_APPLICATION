package com.aspiresys.grocerymanagement;

import java.sql.SQLException;

public class GroceryManagementApplication {
    public static void main(String[] args) {
        try {
            Database database = new Database();
            GroceryManagement gms = new GroceryManagement(database);
            gms.start();
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }
}
