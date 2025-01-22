package com.aspiresys.grocerymanagement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private Connection connection;

    public Database() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/grocery_database"; // Update with your database info
        String user = "root"; // Your database username
        String password = "Aspire@123"; // Your database password
        connection = DriverManager.getConnection(url, user, password);
    }

    // User methods
    public void registerUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword()); // Password should be hashed in a real application
            pstmt.setString(3, user.getRole());
            pstmt.executeUpdate();
        }
    }

    public User loginUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password); // In a real application, use hashed passwords
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getString("username"), rs.getString("password"), rs.getString("role"));
            } else {
                return null; // User not found
            }
        }
    }

    // Grocery item methods
    public void addItem(GroceryItem item) throws SQLException {
        String sql = "INSERT INTO grocery_items (name, category, price, quantity, discount) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, item.getName());
            pstmt.setString(2, item.getCategory());
            pstmt.setDouble(3, item.getPrice());
            pstmt.setInt(4, item.getQuantity());
            pstmt.setDouble(5, item.getDiscount());
            pstmt.executeUpdate();
        }
    }

    public void updateItem(GroceryItem item) throws SQLException {
        String sql = "UPDATE grocery_items SET name=?, category=?, price=?, quantity=?, discount=? WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, item.getName());
            pstmt.setString(2, item.getCategory());
            pstmt.setDouble(3, item.getPrice());
            pstmt.setInt(4, item.getQuantity());
            pstmt.setDouble(5, item.getDiscount());
            pstmt.setInt(6, item.getId());
            pstmt.executeUpdate();
        }
    }

    public void removeItem(int id) throws SQLException {
        String sql = "DELETE FROM grocery_items WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<GroceryItem> getAllItems() throws SQLException {
        List<GroceryItem> items = new ArrayList<>();
        String sql = "SELECT * FROM grocery_items";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                items.add(new GroceryItem(rs.getInt("id"), rs.getString("name"), 
                    rs.getString("category"), rs.getDouble("price"), 
                    rs.getInt("quantity"), rs.getDouble("discount")));
            }
        }
        return items;
    }

    public List<GroceryItem> searchItems(String searchTerm) throws SQLException {
        List<GroceryItem> items = new ArrayList<>();
        String sql = "SELECT * FROM grocery_items WHERE name LIKE ? OR category LIKE ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, "%" + searchTerm + "%");
            pstmt.setString(2, "%" + searchTerm + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    items.add(new GroceryItem(rs.getInt("id"), rs.getString("name"), 
                        rs.getString("category"), rs.getDouble("price"), 
                        rs.getInt("quantity"), rs.getDouble("discount")));
                }
            }
        }
        return items;
    }
}
