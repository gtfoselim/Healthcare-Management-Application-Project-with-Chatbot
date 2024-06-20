package com.visita.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private static Connection databaseConnection;

    public static Connection getConnection() {
        String databaseUser = "root"; // Replace with your database username
        String databasePassword = ""; // Replace with your database password
        String url = "jdbc:mysql://localhost:3306/sympi5"; // Connection URL for your database

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseConnection = DriverManager.getConnection(url, databaseUser, databasePassword);
            System.out.println("Database connection established.");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
        return databaseConnection;
    }
}
