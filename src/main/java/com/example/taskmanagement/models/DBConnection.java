package com.example.taskmanagement.models;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static Connection conn; // Connection object to interact with the database

    // Method to establish and return a database connection
    public static Connection getConnection() {
        String url = "jdbc:postgresql:taskmanagement"; // Database URL
        String user = "postgres"; // Database username
        String password = "password"; // Database password

        try {
            // Attempt to establish a connection to the database
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace(); // Print any exceptions that occur
        }
        return conn; // Return the established connection
    }
}
