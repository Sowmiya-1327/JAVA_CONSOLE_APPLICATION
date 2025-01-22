package com.aspiresys.grocerymanagement;

public class User {
    private String username;
    private String password;
    private String role; // User role

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role; // Assign role
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role; // New method to get role
    }
}






