package com.visita.models;

public class Admin extends User {
    // Additional attributes specific to Patient

    public Admin(String username, String email, String phone, String password, String token, String photo, String[] role, String fullname) {
        super(0, username, email, phone, password, token,photo, 0, role, fullname);
    }

    public Admin() {
        // Call the superclass constructor with default values or leave it empty
        super(0, "", "", "", "", "1","", 0, new String[]{"admin"}, "");
    }

    public static boolean isValidEmail(String email) {
        // Regular expression for validating email format
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(regex);
    }

    public static boolean isValidPhoneNumber(String phone) {
        // Regular expression to match a string containing exactly 8 digits
        String regex = "\\d{8}";
        return phone.matches(regex);
    }

    public static boolean isValidPassword(String password) {
        // Validate password length (e.g., minimum length 6)
        return password.length() >= 6;
    }
    public static boolean isValidFullname(String fullname) {
        // Regular expression for validating fullname (letters and spaces only)
        String regex = "^[a-zA-Z ]+$";
        return fullname.matches(regex);
    }

}
