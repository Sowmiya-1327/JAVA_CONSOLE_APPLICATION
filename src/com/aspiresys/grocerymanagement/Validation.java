package com.aspiresys.grocerymanagement;

import java.util.regex.Pattern;

public class Validation {
    // Regex patterns
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9]{3,15}$"; // 3 to 15 alphanumeric characters
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-zA-Z]).{6,20}$"; // 6 to 20 characters with at least one letter and one number

    public static boolean isValidUsername(String username) {
        return Pattern.matches(USERNAME_REGEX, username);
    }

    public static boolean isValidPassword(String password) {
        return Pattern.matches(PASSWORD_REGEX, password);
    }
}
